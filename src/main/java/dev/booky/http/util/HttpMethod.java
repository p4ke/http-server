package dev.booky.http.util;

import java.util.Objects;

public final class HttpMethod {

    // https://www.rfc-editor.org/rfc/rfc2616#section-5.1.1
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod PUT = new HttpMethod("PUT");
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    public static final HttpMethod TRACE = new HttpMethod("TRACE");
    public static final HttpMethod CONNECT = new HttpMethod("CONNECT");

    private final String method;

    public HttpMethod(final String method) {
        this.method = method;
    }

    public static HttpMethod parseMethod(final HttpReader reader) {
        reader.skipLWS();
        final String method = reader.readToken();
        return switch (method) {
            case "OPTIONS" -> OPTIONS;
            case "GET" -> GET;
            case "HEAD" -> HEAD;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "DELETE" -> DELETE;
            case "TRACE" -> TRACE;
            case "CONNECT" -> CONNECT;
            default -> new HttpMethod(method);
        };
    }

    public String getMethod() {
        return this.method;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        final HttpMethod that = (HttpMethod) obj;
        return Objects.equals(this.method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.method);
    }

    @Override
    public String toString() {
        return this.method;
    }
}
