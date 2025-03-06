package dev.booky.http;

import dev.booky.http.log.Logger;
import dev.booky.http.log.LoggerFactory;
import dev.booky.http.protocol.HttpHeaders;
import dev.booky.http.protocol.HttpRequest;
import dev.booky.http.protocol.HttpResponse;
import dev.booky.http.protocol.HttpUri.UriImpl;
import dev.booky.http.util.HttpMethod;
import dev.booky.http.util.HttpReader;
import dev.booky.http.util.MimeType;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpStatus.STATUS_BAD_REQUEST;
import static dev.booky.http.protocol.HttpStatus.STATUS_METHOD_NOT_ALLOWED;
import static dev.booky.http.protocol.HttpStatus.STATUS_NOT_FOUND;
import static dev.booky.http.protocol.HttpStatus.STATUS_OK;
import static java.net.StandardSocketOptions.SO_REUSEADDR;
import static java.net.StandardSocketOptions.TCP_NODELAY;

@NullMarked
public class HttpServer implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger("Http");

    private final ServerSocket socket;
    private final ServerParameters params;
    private final ExecutorService executor;

    private final AtomicInteger threadCount = new AtomicInteger();

    private HttpServer(
            final ServerSocket socket,
            final ServerParameters params
    ) {
        this.socket = socket;
        this.params = params;

        // Es wird ein Thread-Pool erstellt, damit mehrere Http-Anfragen
        // gleichzeitig verarbeitet werden können
        this.executor = Executors.newCachedThreadPool(this::constructThread);
    }

    public static HttpServer createAndBind(
            final SocketAddress address,
            final ServerParameters params
    ) throws IOException {
        final ServerSocket socket = new ServerSocket();
        try {
            socket.setOption(TCP_NODELAY, true);
        } catch (final UnsupportedOperationException ignored) {
            // Einige Betriebssysteme (z.B. Windows) unterstützen nicht den TCP_NODELAY Socket-Parameter
        }

        // Das TCP-Protokoll würde nach einem schnellen Programmneustart die
        // IP-Adresse nicht direkt wieder freigeben, da noch potenzielle TCP-Pakete
        // im Netzwerk sein könnten; mit dieser Option wird trotzdem direkt die IP-Adresse
        // wieder verwendet; siehe https://stackoverflow.com/a/3233022 für mehr Informationen
        socket.setOption(SO_REUSEADDR, true);

        // Schließlich wird die TCP-Socket erstellt und eine Info-Nachricht
        // in die Konsole gesendet
        socket.bind(address);
        LOGGER.info("Http socket bound on http://%s/",
                StringUtil.stringifyBindAddress(address));

        return new HttpServer(socket, params);
    }

    private Thread constructThread(final Runnable runnable) {
        // Es wird ein neuer Anfragen-Thread erstellt und benannt
        final SocketAddress address = this.socket.getLocalSocketAddress();
        final Thread thread = new Thread(runnable, "Http Socket " + address
                + " Thread #" + this.threadCount.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }

    public void tryAccept() throws IOException {
        // Es wird gewartet, bis eine Verbindung zu einem Browser verfügbar ist
        final Socket socket = this.socket.accept();
        // Direkt nach dem Akzeptieren einer Browser-Verbindung wird zu einem neuen Thread
        // gewechselt, um die Dauer des Blockierens der Verbindungsannahme so weit wie möglich
        // zu reduzieren
        this.executor.execute(() -> {
            final String addressString = StringUtil.stringifyAddress(socket.getRemoteSocketAddress());
            LOGGER.info("Accepted socket connection from %s", addressString);

            // Im Http-1.1-Protokoll bestehen Anfragen aus Metadaten und dem Inhalt
            // die Metadaten sind reiner Text, der Inhalt kann auch im Binärformat vorliegen
            try (socket; final InputStream input = socket.getInputStream();
                 final Reader inputReader = new InputStreamReader(input);
                 final BufferedReader bufferedReader = new BufferedReader(inputReader)) {

                // Der HttpReader besteht aus mehreren Hilfs-Methoden, mit welchen
                // öfters verwendete Bestandteile einer Http-Anfrage ausgelesen werden können
                final HttpReader reader = new HttpReader(bufferedReader);

                // Falls eine Anfrage komplett leer ist, wird
                // die Bearbeitung komplett übersprungen
                if (!reader.isReadable()) {
                    return;
                }

                // Hier wird die Anfrage eingelesen, um sowohl Metadaten
                // als auch (falls vorhanden) den Inhalt auszulesen
                final HttpRequest request = HttpRequest.parseRequest(reader);

                // Nach dem erfolgreichen Auslesen der Http-Anfrage wird
                // die Anfrage verarbeitet - solange bleibt die Browser-Verbindung noch geöffnet
                this.handleMessage(socket, request);
            } catch (final Throwable throwable) {
                // Falls es einen Fehler bei dem Lesen der Anfrage gab,
                // wird eine Fehlernachricht abgesendet
                LOGGER.error("Error while reading request from %s", addressString, throwable);
            }
        });
    }

    public void handleMessage(final Socket socket, final HttpRequest request) {
        // Der ausgehende Netzwerk-Stream wird erstellt und gepuffert
        try (final OutputStream output = socket.getOutputStream();
             final Writer outputWriter = new OutputStreamWriter(output);
             final BufferedWriter writer = new BufferedWriter(outputWriter)) {
            // Basierend auf der Anfrage wird eine Antwort erstellt
            final HttpResponse response = this.buildResponse(request);
            // Nach dem Erstellen der Antwort wird eine Infonachricht in die Konsole gesendet
            LOGGER.info("Handled %s %s %s from %s with %s", request.getVersion(), request.getMethod(),
                    request.getUri(), StringUtil.stringifyAddress(socket.getRemoteSocketAddress()),
                    response.getStatus().toString());
            // Schließlich wird die Antwort in die Browser-Verbindung geschrieben
            response.writeTo(output, writer);
        } catch (final Throwable throwable) {
            // Falls es einen Fehler während der Verarbeitung
            // gab, wird eine Fehlernachricht abgesendet
            LOGGER.error("Handled %s %s %s from %s with error", request.getVersion(), request.getMethod(),
                    request.getUri(), StringUtil.stringifyAddress(socket.getRemoteSocketAddress()), throwable);
        }
    }

    // Erstellt eine Http-Antwort mit dem Inhalt der angefragten Datei
    public HttpResponse buildResponse(final HttpRequest request) {
        // Für Dateianfragen sind nur Http-GET-Anfragen und Http-HEAD-Anfragen erlaubt
        final HttpMethod method = request.getMethod();
        if (method != HttpMethod.GET && method != HttpMethod.HEAD) {
            return request.buildError(STATUS_METHOD_NOT_ALLOWED);
        }
        // Das Http-Protokoll erlaubt auch URIs wie z.B. "*";
        // sowas ist nicht für eine Dateiabfrage erlaubt
        if (!(request.getUri() instanceof UriImpl)) {
            return request.buildError(STATUS_BAD_REQUEST, "Invalid request URI");
        }
        // Aus der Anfrage wird der Zieldateipfad extrahiert, basierend auf den Server-Parametern
        final Path targetPath = this.extractPath((UriImpl) request.getUri());
        // Falls die Datei NICHT existiert, wird ein Fehler zurückgegeben
        if (!Files.isRegularFile(targetPath)) {
            return request.buildError(STATUS_NOT_FOUND, "Path " + targetPath + " not found");
        }

        // Die Dateigröße wird ausgelesen, um sie bereits vorher über die
        final long fileSize;
        try {
            fileSize = Files.size(targetPath);
        } catch (final IOException exception) {
            throw new RuntimeException("Error while reading file size from " + targetPath);
        }

        // Basierend auf dem Dateipfad wird ein MIME-Typ "geraten";
        // falls nichts erkannt werden kann, wird es als Binärdatei gesendet
        final MimeType mimeType = MimeType.guessFromPathName(targetPath);
        final Map<String, String> headers = Map.of(
                "content-type", mimeType.toString(),
                "content-length", Long.toString(fileSize)
        );

        // Die Antwort wird gebaut - der Dateiinhalt wird als Stream-Supplier angegeben,
        // damit die Datei direkt zur Antwort "gestreamt" werden kann, ohne vorher komplett im RAM
        // liegen zu müssen
        return new HttpResponse(request.getVersion(), STATUS_OK, HttpHeaders.headers(headers),
                method == HttpMethod.GET ? () -> Files.newInputStream(targetPath) : null);
    }

    private Path extractPath(final UriImpl uri) {
        // Relativ vom Server-Verzeichnis wird ein Dateipfad aufgelöst
        final Path targetPath = uri.resolvePath(this.params.rootDir());
        // Falls der Dateipfad kein Ordner ist, wird dieser direkt zurückgegeben
        if (!Files.isDirectory(targetPath)) {
            return targetPath;
        }
        // Andernfalls wird geprüft, ob irgendeine der erlaubten Index-Dateien
        // im gegebenen Ordner existiert
        final Optional<Path> indexPath = this.params.indexFiles().stream()
                .map(targetPath::resolve)
                .filter(Files::isRegularFile)
                .findFirst();
        // Falls eine Index-Datei existiert, wird diese zurückgegeben;
        // andernfalls wird einfach der Ursprungspfad des Ordners zurückgegeben
        return indexPath.orElse(targetPath);
    }

    @Override
    public void close() throws IOException {
        // Wenn der Http-Server geschlossen wird,
        // wird auch die TCP-Socket geschlossen
        this.socket.close();
    }

    // Konfigurationsparameter für den Http-Server
    public record ServerParameters(
            Path rootDir,
            List<String> indexFiles
    ) {

        private static final List<String> DEFAULT_INDEX_FILES = List.of("index.html");

        public ServerParameters(final Path rootDir) {
            this(rootDir, DEFAULT_INDEX_FILES);
        }
    }
}
