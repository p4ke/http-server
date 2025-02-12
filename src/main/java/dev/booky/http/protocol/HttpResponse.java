package dev.booky.http.protocol;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static dev.booky.http.protocol.HttpDefinitions.CRLF;
import static dev.booky.http.protocol.HttpDefinitions.SP;

public class HttpResponse {

    private final HttpVersion version;
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpResponse(
            final HttpVersion version,
            final HttpStatus status,
            final HttpHeaders headers,
            final byte[] body
    ) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public void writeTo(final OutputStream output) throws IOException {
        try (final Writer writer = new OutputStreamWriter(output);
             final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(this.version.toString());
            bufferedWriter.write(SP);
            bufferedWriter.write(this.status.toString());
            bufferedWriter.write(CRLF);
            this.headers.writeTo(bufferedWriter);
            bufferedWriter.write(CRLF);
        }
        output.write(this.body);
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

    public byte[] getBody() {
        return this.body;
    }
}
