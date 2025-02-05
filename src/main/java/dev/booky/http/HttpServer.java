package dev.booky.http;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.file.Path;

@NullMarked
public class HttpServer implements AutoCloseable {

    private final ServerSocket socket;
    private final ServerParameters params;

    private HttpServer(
            final ServerSocket socket,
            final ServerParameters params
    ) {
        this.socket = socket;
        this.params = params;
    }

    public static HttpServer create(
            final SocketAddress address,
            final ServerParameters params
    ) throws IOException {
        final ServerSocket socket = new ServerSocket();
        socket.bind(address);
        return new HttpServer(socket, params);
    }

    public ServerSocket getSocket() {
        return this.socket;
    }

    public ServerParameters getParams() {
        return this.params;
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    public record ServerParameters(
            Path rootDir
    ) {
    }
}
