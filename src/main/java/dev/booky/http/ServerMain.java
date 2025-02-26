package dev.booky.http;

import dev.booky.http.HttpServer.ServerParameters;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

@NullMarked
public final class ServerMain {

    private static final int DEFAULT_HTTP_PORT = 8080;
    private static final String DEFAULT_HTTP_DIR = "demo";

    private ServerMain() {
    }

    public static void main(final String[] args) throws IOException {
        // Aus den Programmargumenten wird sowohl Http-Server-Port als
        // auch der Dateipfad ausgelesen, von welchem Dateien bereitgestellt werden
        final int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_HTTP_PORT;
        final Path path = Path.of(args.length > 1 ? args[1] : DEFAULT_HTTP_DIR);

        // Es wird eine IP-Adresse mit dem gegebenen Port erstellt
        final InetSocketAddress address = new InetSocketAddress("0.0.0.0", port);
        // Die Standard-Http-Server-Parameter werden mit dem gegebenen Dateipfad erstellt
        final ServerParameters params = new ServerParameters(path);

        // Der Http-Server wird mithilfe der Parameter erstellt
        // und an die erstellte IP-Adresse gebunden
        try (final HttpServer server = HttpServer.createAndBind(address, params)) {
            // Solange das Programm läuft, wird versucht eine
            // Verbindung eines Browsers anzunehmen
            while (true) {
                server.tryAccept();
            }
        }
    }
}
