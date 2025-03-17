package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

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

    public Path resolvePath(final Path root) {
        // TODO ensure path is safe
        final String uriPath = this.uri.getPath();
        if (uriPath.isEmpty() || "/".equals(uriPath)) {
            return root;
        }
        if (uriPath.charAt(0) == '/') {
            return root.resolve(uriPath.substring(1));
        }
        return root.resolve(uriPath);
    }

    @Override
    public String toString() {
        return this.uri.toString();
    }
}
