package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import dev.booky.http.util.StringUtil;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.booky.http.protocol.HttpDefinitions.CRLF;

@NullMarked
public class HttpHeaders {

    private static final HttpHeaders EMPTY = new HttpHeaders(Map.of());

    private final Map<String, String> headers;

    private HttpHeaders(final Map<String, String> headers) {
        this.headers = headers.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                // header names are case-insensitive https://www.rfc-editor.org/rfc/rfc2616#section-4.2
                .map(entry -> Map.entry(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Contract("null, null -> null; !null, _ -> !null; _, !null -> !null")
    private static @Nullable String joinHeaderValues(@Nullable final String value1, @Nullable final String value2) {
        // this is fine behavior and should not change any semantics, according to
        // the bottom of https://www.rfc-editor.org/rfc/rfc2616#section-4.2
        if (value1 == null) return value2;
        if (value2 == null) return value1;
        return value1 + ',' + value2;
    }

    public static HttpHeaders headers(final Map<String, String> headers) {
        return new HttpHeaders(headers);
    }

    public static HttpHeaders headersEmpty() {
        return EMPTY;
    }

    public static HttpHeaders parseHeaders(final HttpReader reader) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        do {
            if (CRLF.equals(reader.peek(CRLF.length()))) {
                // if there still is CRLF after the initial CRLF,
                // end parsing of headers as the message body will follow
                break;
            }
            final String name = reader.readLineUntil(':');
            reader.skip().skipLWS();
            final String value = reader.readMultiLine();
            headers.compute(name, (key, existingValue) -> joinHeaderValues(existingValue, value));
        } while (reader.skipCRLF() && reader.isReadable(CRLF.length()));
        return new HttpHeaders(headers);
    }

    public void writeTo(final BufferedWriter writer) throws IOException {
        for (final Map.Entry<String, String> entry : this.headers.entrySet()) {
            writer.write(entry.getKey());
            writer.write(": ");
            writer.write(entry.getValue());
        }
    }

    public @Nullable String getHeader(final String name) {
        return this.headers.get(name.toLowerCase(Locale.ROOT));
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public String toString() {
        return this.headers.toString();
    }
}
