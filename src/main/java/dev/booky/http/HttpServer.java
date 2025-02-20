package dev.booky.http;

import dev.booky.http.log.Logger;
import dev.booky.http.log.LoggerFactory;
import dev.booky.http.protocol.HttpRequest;
import dev.booky.http.protocol.HttpResponse;
import dev.booky.http.util.HttpReader;
import dev.booky.http.util.StringUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpHeaders.headersEmpty;
import static dev.booky.http.protocol.HttpStatus.STATUS_OK;
import static java.net.StandardSocketOptions.SO_REUSEADDR;
import static java.net.StandardSocketOptions.TCP_NODELAY;

@NullMarked
public class HttpServer implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger("Http");

    private final ServerSocket socket;
    private final ServerParameters params;
    private final ExecutorService executor;

    private HttpServer(
            final ServerSocket socket,
            final ServerParameters params
    ) {
        this.socket = socket;
        this.params = params;
        this.executor = Executors.newCachedThreadPool(this::constructThread);
    }

    public static HttpServer create(
            final SocketAddress address,
            final ServerParameters params
    ) throws IOException {
        final ServerSocket socket = new ServerSocket();
        try {
            socket.setOption(TCP_NODELAY, true);
        } catch (final UnsupportedOperationException ignored) {
            // expected on specific operating systems
        }
        // instantly bind to address regardless of what TCP wants
        socket.setOption(SO_REUSEADDR, true);

        socket.bind(address);
        LOGGER.info("Http socket bound on http://%s/", StringUtil.stringifyAddress(address));

        return new HttpServer(socket, params);
    }

    private Thread constructThread(final Runnable runnable) {
        final SocketAddress address = this.socket.getLocalSocketAddress();
        final Thread thread = new Thread(runnable, "Http Socket " + address + " Thread");
        thread.setDaemon(true);
        return thread;
    }

    public void tryAccept() throws IOException {
        final Socket socket = this.socket.accept();
        this.executor.execute(() -> {
            LOGGER.info("Accepted socket connection from %s",
                    StringUtil.stringifyAddress(socket.getRemoteSocketAddress()));
            try (socket; final InputStream input = socket.getInputStream();
                 final Reader inputReader = new InputStreamReader(input);
                 final BufferedReader bufferedReader = new BufferedReader(inputReader)) {
                final HttpReader reader = new HttpReader(bufferedReader);
                this.handleMessage(socket, HttpRequest.parseMessage(reader));
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public void handleMessage(final Socket socket, final HttpRequest message) throws IOException {
        try (final OutputStream output = socket.getOutputStream();
             final Writer outputWriter = new OutputStreamWriter(output);
             final BufferedWriter writer = new BufferedWriter(outputWriter)) {
            final HttpResponse resp = new HttpResponse(
                    message.getVersion(), STATUS_OK, headersEmpty(),
                    """
                            Version: %s
                            URI: %s
                            Method: %s
                            Headers: %s
                            """.formatted(
                            message.getVersion().toString(),
                            message.getUri().toString(),
                            message.getMethod().toString(),
                            message.getHeaders().getHeaders().entrySet().stream()
                                    .map(entry -> "\n  %s = %s".formatted(entry.getKey(), entry.getValue()))
                                    .collect(Collectors.joining())
                    ).stripIndent().getBytes(StandardCharsets.UTF_8)
            );
            resp.writeTo(output, writer);
            LOGGER.info("Handled %s %s %s from %s", message.getVersion(), message.getMethod(),
                    message.getUri(), StringUtil.stringifyAddress(socket.getRemoteSocketAddress()));
        } catch (final Throwable throwable) {
            LOGGER.error("Handled %s %s %s from %s with error", message.getVersion(), message.getMethod(),
                    message.getUri(), StringUtil.stringifyAddress(socket.getRemoteSocketAddress()), throwable);
        }
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public ServerParameters getParams() {
        return this.params;
    }

    public record ServerParameters(
            Path rootDir
    ) {
    }
}
