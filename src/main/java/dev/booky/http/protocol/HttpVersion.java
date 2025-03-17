package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import java.io.IOException;

// Die unterstützen Http-Versionen; auch wenn schon HTTP/3 auf
// Quic-UDP-Protokoll-Basis existiert, unterstützt diese Http-Server-Implementation
// bis jetzt nur unverschlüsseltes HTTP/1.0 und HTTP/1.1
public final class HttpVersion {

    public static final HttpVersion HTTP_1_0 = new HttpVersion(1, 0);
    public static final HttpVersion HTTP_1_1 = new HttpVersion(1, 1);

    private final int major;
    private final int minor;
    private final String string;

    private HttpVersion(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
        this.string = "HTTP/%s.%s".formatted(major, major);
    }

    public static HttpVersion parseVersion(final HttpReader reader) throws IOException {
        // Aus dem Http-Reader wird die Http-Version gelesen; da zum aktuellen Zeitpunkt nur HTTP/1.0
        // und HTTP/1.1 unterstützt wird, wird bei einem anderen Wert eine Fehlermeldung ausgegeben
        final String version = reader.read("HTTP/0.0".length());
        return switch (version) {
            case "HTTP/1.0" -> HTTP_1_0;
            case "HTTP/1.1" -> HTTP_1_1;
            default -> throw new IllegalArgumentException("Illegal http version: " + version);
        };
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
