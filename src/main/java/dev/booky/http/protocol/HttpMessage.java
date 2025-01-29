package dev.booky.http.protocol;

import dev.booky.http.util.HttpMethod;
import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HttpMessage {

    private final HttpMethod method;
    private final HttpUri uri;
    private final HttpHeaders headers;

    public HttpMessage(
            final HttpMethod method,
            final HttpUri uri,
            final HttpHeaders headers
    ) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
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
        // TODO read http version
        reader.skipLWS(); // skip any LWS instead of only CRLF

        final HttpHeaders headers = HttpHeaders.parseHeaders(reader);

        // TODO parse body

        return new HttpMessage(method, uri, headers);
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public HttpUri getUri() {
        return this.uri;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
