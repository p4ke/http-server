package dev.booky.http.protocol;

import dev.booky.http.util.HttpMethod;
import dev.booky.http.util.HttpReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpHeaders.headers;
import static dev.booky.http.util.MimeType.TYPE_PLAIN_UTF8;

@NullMarked
public class HttpRequest {

    private final HttpMethod method;
    private final HttpUri uri;
    private final HttpVersion version;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpRequest(
            final HttpMethod method,
            final HttpUri uri,
            final HttpVersion version,
            final HttpHeaders headers,
            final byte[] body
    ) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest parseRequest(final HttpReader reader) throws IOException {
        // the protocol says to only skip CRLF, but we skip any whitespaces
        // we encounter: https://www.rfc-editor.org/rfc/rfc2616#section-4.1
        reader.skipLWS();

        // Request-Line = Method SP Request-URI SP HTTP-Version CRLF
        // (https://www.rfc-editor.org/rfc/rfc2616#section-5.1)
        final HttpMethod method = HttpMethod.parseMethod(reader);
        reader.skipLWS(); // skip any LWS instead of only SP
        final HttpUri uri = HttpUri.parseUri(reader);
        reader.skipLWS(); // skip any LWS instead of only SP
        final HttpVersion version = HttpVersion.parseVersion(reader);
        reader.skipLWS(); // skip any LWS instead of only CRLF

        final HttpHeaders headers = HttpHeaders.parseHeaders(reader);

        final String bodyString = reader.getRemaining();
        final byte[] bodyBytes = bodyString.getBytes(StandardCharsets.UTF_8);

        return new HttpRequest(method, uri, version, headers, bodyBytes);
    }

    public HttpResponse buildError(final HttpStatus status) {
        return this.buildError(status, status.getName());
    }

    public HttpResponse buildError(final HttpStatus status, final String message) {
        final Map<String, String> headers = Map.of("content-type", TYPE_PLAIN_UTF8.getType());
        return new HttpResponse(this.version, status, headers(headers),
                message.getBytes(StandardCharsets.UTF_8));
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public HttpUri getUri() {
        return this.uri;
    }

    public HttpVersion getVersion() {
        return this.version;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public byte[] getBody() {
        return this.body;
    }
}
