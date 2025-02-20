package dev.booky.http.protocol;

import dev.booky.http.util.CheckedSupplier;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpDefinitions.CRLF;
import static dev.booky.http.protocol.HttpDefinitions.SP;

@NullMarked
public class HttpResponse {

    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final CheckedSupplier<InputStream, IOException> body;

    public HttpResponse(
            final HttpVersion version,
            final HttpStatus status,
            final HttpHeaders headers,
            final byte[] body
    ) {
        this(version, status, headers, () -> new ByteArrayInputStream(body));
    }

    public HttpResponse(
            final HttpVersion version,
            final HttpStatus status,
            final HttpHeaders headers,
            final CheckedSupplier<InputStream, IOException> body
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
        writer.write(this.version.toString());
        writer.write(SP);
        writer.write(this.status.toString());
        writer.write(CRLF);
        this.headers.writeTo(writer);
        writer.write(CRLF);
        writer.write(CRLF);
        writer.flush(); // flush!

        // write entire body to output
        try (final InputStream input = this.body.get()) {
            input.transferTo(output);
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

    public CheckedSupplier<InputStream, IOException> getBody() {
        return this.body;
    }
}
