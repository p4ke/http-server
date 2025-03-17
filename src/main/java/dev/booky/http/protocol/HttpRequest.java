package dev.booky.http.protocol;

import dev.booky.http.util.HttpMethod;
import dev.booky.http.util.HttpReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpDefinitions.HEADER_CONTENT_LENGTH;
import static dev.booky.http.protocol.HttpDefinitions.HEADER_CONTENT_TYPE;
import static dev.booky.http.util.MimeType.TYPE_PLAIN_UTF8;

// Das Objekt, welche alle Attribute einer Http-Anfrage beinhaltet
// und die Logik, um diese aus Text auszulesen
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
        // Laut dem Http-Protokoll sollte beim Start einer Anfrage eigentlich nur
        // ein Zeilensprung übersprungen werden, allerdings überspringen wir hier
        // zur Sicherheit alle Weißzeichen, siehe https://www.rfc-editor.org/rfc/rfc2616#section-4.1
        reader.skipLWS();

        // Die erste Zeile einer Http-Anfrage sieht wie folgt aus:
        // Request-Line = Method SP Request-URI SP HTTP-Version CRLF
        // (https://www.rfc-editor.org/rfc/rfc2616#section-5.1)

        // Zuerst wird die Http-Methode eingelesen:
        final HttpMethod method = HttpMethod.parseMethod(reader);
        reader.skipLWS(); // statt nur einem Leerzeichen überspringen wir hier zur Sicherheit alle Weißzeichen
        final HttpUri uri = HttpUri.parseUri(reader);
        reader.skipLWS(); // statt nur einem Leerzeichen überspringen wir hier zur Sicherheit alle Weißzeichen
        final HttpVersion version = HttpVersion.parseVersion(reader);
        reader.skipLWS(); // statt nur einem Zeilensprung überspringen wir hier zur Sicherheit alle Weißzeichen

        // Nach der Anfragen-Zeile folgen die Http-Header
        final HttpHeaders headers = HttpHeaders.parseHeaders(reader);

        // Schließlich wird der restliche Anfragen-Inhalt als Zeichenkette
        // eingelesen und zu einem UTF-8-kodiertem Byte-Array umgewandelt;
        // zum aktuellen Zeitpunkt werden durch den Http-Reader dadurch
        // keine Binäranfragen unterstützt, sondern nur UTF-8-kodierte Textdateien
        final String bodyString = reader.getRemaining();
        final byte[] bodyBytes = bodyString.getBytes(StandardCharsets.UTF_8);

        return new HttpRequest(method, uri, version, headers, bodyBytes);
    }

    // Eine Hilfsmethode, um basierend auf dieser Http-Anfrage eine
    // einfache Http-Fehlermeldung-Antwort zu erstellen
    public HttpResponse buildError(final HttpStatus status, final String message) {
        final byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        final HttpHeaders headers = HttpHeaders.buildResponseHeaders(Map.of(
                HEADER_CONTENT_TYPE, TYPE_PLAIN_UTF8.getType(),
                HEADER_CONTENT_LENGTH, Integer.toString(messageBytes.length)
        ));
        return new HttpResponse(this.version, status, headers, messageBytes);
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
