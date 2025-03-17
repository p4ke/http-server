package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

// Siehe https://www.rfc-editor.org/rfc/rfc2616#section-5.1.2;
// eine URI kann sowohl ein einzelnes "*" als auch eine absolute URI/Pfad
// sein - Java's URI Klasse unterstützt beides
@NullMarked
public record HttpUri(URI uri) {

    public static HttpUri parseUri(final HttpReader reader) throws IOException {
        final String uriString = reader.readLineUntilLWS();
        return new HttpUri(URI.create(uriString));
    }

    public @Nullable Path resolvePath(final Path root) {
        final String uriPath = this.uri.getPath();
        if (uriPath.isEmpty() || "/".equals(uriPath)) {
            // Falls der Pfad leer ist oder nur aus einem einzigen "/" besteht, wird direkt
            // das Root-Verzeichnis zurückgegeben
            return root;
        }
        // Falls der Pfad mit einem "/" anfängt, wird dieses weggeschnitten,
        // da wir hier relativ von dem Root-Verzeichnis auflösen wollen
        final Path targetPath = uriPath.charAt(0) == '/'
                ? root.resolve(uriPath.substring(1))
                : root.resolve(uriPath);
        // Hier wird überprüft, dass ein "Hacker" nicht mithilfe von ".."
        // aus dem Root-Verzeichnis herauswandern kann und somit Zugriff auf alle Dateien
        // im System hätte
        if (targetPath.toAbsolutePath().startsWith(root)) {
            // Der Zielpfad ist immer noch im Root-Verzeichnis und damit gültig
            return targetPath;
        }
        // Die Anfrage ist zu hoch gegangen und damit ungültig
        return null;
    }

    @Override
    public String toString() {
        return this.uri.toString();
    }
}
