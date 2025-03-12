package dev.booky.http.protocol;

import dev.booky.http.util.CheckedSupplier;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static dev.booky.http.protocol.HttpDefinitions.CRLF;
import static dev.booky.http.protocol.HttpDefinitions.SP;

@NullMarked
public final class HttpResponse {

    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeaders headers;

    // Um nicht den kompletten Dateiinhalt auf einmal in dem RAM laden zu müssen,
    // wird hier ein java InputStream-Supplier abgespeichert, welcher dann
    // direkt an den Browser übertragen wird, ohne vollständig im RAM liegen zu müssen
    private final @Nullable CheckedSupplier<InputStream, IOException> body;

    // Ein Hilfs-Construktor, mit welchem ein einfaches Byte-Array
    // als Antwortsinhalt genommen wird
    public HttpResponse(
            final HttpVersion version,
            final HttpStatus status,
            final HttpHeaders headers,
            final byte @Nullable [] body
    ) {
        this(version, status, headers, body != null
                ? () -> new ByteArrayInputStream(body) : null);
    }

    public HttpResponse(
            final HttpVersion version,
            final HttpStatus status,
            final HttpHeaders headers,
            final @Nullable CheckedSupplier<InputStream, IOException> body
    ) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public void writeTo(
            final OutputStream output,
            final BufferedWriter writer
    ) throws IOException {
        // Die erste Zeile einer Http-Antwort sieht wie folgt aus:
        // Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
        // (https://www.rfc-editor.org/rfc/rfc2616#section-6.1)
        writer.write(this.version.toString());
        writer.write(SP);
        writer.write(this.status.toString());
        writer.write(CRLF);

        // Auf die Statuszeile der Http-Antwort folgen die Http-Header
        this.headers.writeTo(writer);

        if (this.body != null) {
            // Durch einen doppelten Zeilensprung wird das Ende der Http-Header
            // markiert und der Start des Http-Antwort-Inhalts eingeleitet
            writer.write(CRLF);
        }

        // Der "Writer" wird "gespült"; da wir hier einen "BufferedWriter" haben,
        // müssen zuerst alle gepufferten Textinhalte in den eigentlichen OutputStream
        // "gespült" werden, damit wir den Antwort-Inhalt direkt in den OutputStream
        // übertragen können, ohne das die Antwort falsch angeordnet ist
        writer.flush();

        if (this.body != null) {
            // Schließlich wird sich ein neuer InputStream aus dem Supplier
            // geholt und mit Java-Methoden in den OutputStream "transferiert"
            try (final InputStream input = this.body.get()) {
                input.transferTo(output);
            }
        }
    }

    public HttpVersion getVersion() {
        return this.version;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public @Nullable CheckedSupplier<InputStream, IOException> getBody() {
        return this.body;
    }
}
