package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: casing of header names?
// TODO test parsing headers
@NullMarked
public class HttpHeaders {

    private static final HttpHeaders EMPTY = new HttpHeaders(Map.of());

    private final Map<String, List<String>> headers;

    private HttpHeaders(final Map<String, List<String>> headers) {
        this.headers = Map.copyOf(headers);
    }

    public static HttpHeaders headers(final Map<String, List<String>> headers) {
        return new HttpHeaders(headers);
    }

    public static HttpHeaders headersSimple(final Map<String, String> headers) {
        final Map<String, List<String>> headers0 = new HashMap<>(headers.size());
        for (final Map.Entry<String, String> entry : headers.entrySet()) {
            headers0.put(entry.getKey(), List.of(entry.getValue()));
        }
        return new HttpHeaders(headers0);
    }

    public static HttpHeaders headersEmpty() {
        return EMPTY;
    }

    public static HttpHeaders headersParsed(final HttpReader reader) {
        final Map<String, List<String>> headers = new HashMap<>();
        do {
            final String name = reader.readLineUntil(':');
            final String value = reader.skip().readMultiLine();
            headers.computeIfAbsent(name, __ -> new ArrayList<>()).add(value);
        } while (reader.skipCRLF());
        return new HttpHeaders(headers);
    }

    public List<String> getHeaders(String name) {
        // FIXME
    }
}
