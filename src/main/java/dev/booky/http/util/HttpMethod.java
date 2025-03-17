package dev.booky.http.util;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;

// Eine Hilfsklasse, welche einige Konstante und Logik für Http-Methoden enthält
@NullMarked
public final class HttpMethod {

    // Die Standard-Http-Methoden, siehe https://www.rfc-editor.org/rfc/rfc2616#section-5.1.1
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

    // Liest die benutzte Http-Methode aus dem Http-Reader
    public static HttpMethod parseMethod(final HttpReader reader) throws IOException {
        reader.skipLWS(); // zuerst werden wieder Weißzeichen übersprungen
        // Danach wird bis zum nächsten Weißzeichen Text gelesen
        final String method = reader.readLineUntilLWS();
        // Schließlich wird die Methode zurückgegeben - die standardisierten
        // Methoden haben einige Konstanten oben in der Klasse
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
    public String toString() {
        return this.method;
    }
}
