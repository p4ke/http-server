package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

import java.net.URI;

// https://www.rfc-editor.org/rfc/rfc2616#section-5.1.2
@NullMarked
public sealed interface HttpUri {

    static HttpUri parseUri(final HttpReader reader) {
        final String string = reader.readLineUntilLWS();
        return switch (string) {
            case StarImpl.STAR_STRING -> StarImpl.INSTANCE;
            default -> new UriImpl(URI.create(string));
        };
    }

    @Override
    String toString();

    record StarImpl() implements HttpUri {

        public static final StarImpl INSTANCE = new StarImpl();

        public static final char STAR = '*';
        public static final String STAR_STRING = "*";

        @Override
        public String toString() {
            return STAR_STRING;
        }
    }

    record UriImpl(URI uri) implements HttpUri {

        @Override
        public String toString() {
            return this.uri.toString();
        }
    }
}
