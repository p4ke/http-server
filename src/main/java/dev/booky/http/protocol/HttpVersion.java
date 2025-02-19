package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import java.io.IOException;

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
