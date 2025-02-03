package dev.booky.http.protocol;

import dev.booky.http.util.HttpMethod;
import dev.booky.http.util.HttpReader;
import dev.booky.http.util.HttpHeaderValues;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HttpMessage {

    private final HttpMethod method;
    private final HttpUri uri;
    private final HttpVersion version;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpMessage(
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

    public static HttpMessage parseMessage(final HttpReader reader) {
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
        final HttpHeaderValues headerValues = HttpHeaderValues.fromHeaders(headers);

        // TODO parse body

        return new HttpMessage(method, uri, version, headers);
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
}
