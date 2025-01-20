package dev.booky.http.protocol;

import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

// TODO test parsing headers
@NullMarked
public class HttpHeaders {

    private static final HttpHeaders EMPTY = new HttpHeaders(Map.of());

    private final Map<String, List<String>> headers;

    private HttpHeaders(final Map<String, List<String>> headers) {
        this.headers = headers.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                // header names are case-insensitive https://www.rfc-editor.org/rfc/rfc2616#section-4.2
                .map(entry -> Map.entry(entry.getKey().toLowerCase(Locale.ROOT), List.copyOf(entry.getValue())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
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
            reader.skipLWS();
            final String value = reader.readMultiLine();
            headers.computeIfAbsent(name, __ -> new ArrayList<>()).add(value);
        } while (reader.isReadable() && reader.skipCRLF());
        return new HttpHeaders(headers);
    }

    public List<String> getHeaders(final String name) {
        return this.headers.getOrDefault(name.toLowerCase(Locale.ROOT), List.of());
    }

    public @Nullable String getHeader(final String name) {
        final List<String> headers = this.getHeaders(name);
        return headers.isEmpty() ? null : headers.getLast();
    }

    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        for (final Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
            headers.put(entry.getKey(), entry.getValue().getLast());
        }
        return Collections.unmodifiableMap(headers);
    }

    public Map<String, List<String>> getRawHeaders() {
        return this.headers;
    }
}
