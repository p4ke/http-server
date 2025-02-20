package dev.booky.http.util;

import dev.booky.http.protocol.HttpDefinitions;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpDefinitions.CR;
import static dev.booky.http.protocol.HttpDefinitions.CRLF;
import static dev.booky.http.protocol.HttpDefinitions.LF;
import static dev.booky.http.protocol.HttpDefinitions.SP;

@NullMarked
public final class HttpReader {

    private static final int REMAINING_TEXT_CHAR_BUFFER_SIZE = 8192;
    private static final int MAX_LINE_LENGTH = 256;

    private final Reader reader;

    public HttpReader(final String data) {
        this(new StringReader(data));
    }

    public HttpReader(final Reader reader) {
        this.reader = reader;
    }

    public String readToken() throws IOException {
        return this.readLineUntilLWS();
    }

    public String readLineUntilLWS() throws IOException {
        this.reader.mark(MAX_LINE_LENGTH);
        int charCount = 0;
        int c;
        while ((c = this.reader.read()) != -1 && !HttpDefinitions.isLWS(c)) {
            ++charCount;
        }
        this.reader.reset();
        if (c == -1) {
            throw new IllegalStateException("Can't find LWS in remaining string: '" + this.getRemaining() + "'");
        }
        final char[] chars = new char[charCount];
        final int readChars = this.reader.read(chars);
        assert readChars == charCount;
        return new String(chars);
    }

    public String readLineUntil(final char searchChar) throws IOException {
        this.reader.mark(MAX_LINE_LENGTH);
        int charCount = 0;
        int c;
        while ((c = this.reader.read()) != -1 && c != searchChar) {
            if (c == CR || c == LF) {
                this.reader.reset();
                throw new IllegalArgumentException("Line ends before next occurrence of '"
                        + c + "' in remaining string: '" + this.getRemaining() + "'");
            }
            ++charCount;
        }
        this.reader.reset();
        if (c == -1) {
            throw new IllegalStateException("Can't find character '"
                    + c + "' in remaining string: '" + this.getRemaining() + "'");
        }
        final char[] chars = new char[charCount];
        final int readChars = this.reader.read(chars);
        assert readChars == charCount;
        return new String(chars);
    }

    public boolean skipLWS() throws IOException {
        this.reader.mark(1);
        int c;
        boolean ret = false;
        while ((c = this.reader.read()) != -1 && HttpDefinitions.isLWS(c)) {
            ret = true;
            this.reader.mark(1);
        }
        if (c != -1) {
            this.reader.reset();
        }
        return ret;
    }

    public String readMultiLine() throws IOException {
        final StringBuilder builder = new StringBuilder();
        do {
            // strip trailing whitespace from line
            final String line = this.readSingleLine().stripTrailing();
            builder.append(line).append(SP);
        } while (this.isReadable(CRLF.length())
                // don't continue if CRLF follows
                && !CRLF.equals(this.peek(CRLF.length()))
                && this.skipLWS());
        return builder.toString().stripTrailing();
    }

    public String readSingleLine() throws IOException {
        this.reader.mark(MAX_LINE_LENGTH);
        int charCount = 0;
        boolean endOfLineTrigger = false;
        int c;
        while ((c = this.reader.read()) != -1) {
            if (endOfLineTrigger) {
                if (c == LF) {
                    break; // done!
                }
                throw new IllegalStateException("Expected LF character after CR character; received '" + ((char) c) + "'");
            } else if (c == CR) {
                endOfLineTrigger = true; // http spec requires CRLF line endings
            } else {
                ++charCount;
            }
        }
        this.reader.reset();

        final char[] chars = new char[charCount];
        final int readChars = this.reader.read(chars);
        assert readChars == charCount;
        return new String(chars);
    }

    public boolean skipCRLF() throws IOException {
        if (!this.isReadable(CRLF.length())
                && !CRLF.equals(this.peek(CRLF.length()))) {
            return false;
        }
        this.skip(CRLF.length());
        return true;
    }

    public String read(final int length) throws IOException {
        final char[] chars = new char[length];
        final int actualRead = this.reader.read(chars);
        if (actualRead != length) {
            throw new IllegalArgumentException("Can't read " + length + " chars from reader, " + actualRead
                    + " remaining; end of stream reached");
        }
        return new String(chars);
    }

    public char read() throws IOException {
        final int c = this.reader.read();
        if (c == -1) {
            throw new IllegalArgumentException("Can't read reader, end of stream reached");
        }
        return (char) c;
    }

    public String peek(final int length) throws IOException {
        this.reader.mark(length);
        final char[] chars = new char[length];
        final int actualRead = this.reader.read(chars);
        this.reader.reset();
        if (actualRead != length) {
            throw new IllegalArgumentException("Can't peek " + length + " chars from reader, " + actualRead
                    + " remaining; end of stream reached");
        }
        return new String(chars);
    }

    public char peek() throws IOException {
        this.reader.mark(1);
        final int c = this.reader.read();
        this.reader.reset();
        if (c == -1) {
            throw new IllegalArgumentException("Can't peek reader, end of stream reached");
        }
        return (char) c;
    }

    public HttpReader skip() throws IOException {
        return this.skip(1);
    }

    public HttpReader skip(final int amount) throws IOException {
        final long actualAmount = this.reader.skip(amount);
        if (actualAmount != amount) {
            throw new IOException("Couldn't skip " + amount
                    + ", skipped only " + actualAmount + " instead");
        }
        return this;
    }

    public boolean isReadable() throws IOException {
        return this.isReadable(1);
    }

    public boolean isReadable(final int amount) throws IOException {
        this.reader.mark(amount);
        final long actualAmount = this.reader.skip(amount);
        this.reader.reset();
        return actualAmount == amount;
    }

    public String getRemaining() throws IOException {
        final StringBuilder builder = new StringBuilder();
        // don't read every single char, create a small buffer instead
        final char[] buffer = new char[REMAINING_TEXT_CHAR_BUFFER_SIZE];
        int actualRead;
        while ((actualRead = this.reader.read(buffer)) == REMAINING_TEXT_CHAR_BUFFER_SIZE) {
            builder.append(buffer);
        }
        if (actualRead > 0) {
            builder.append(buffer, 0, actualRead);
        }
        return builder.toString();
    }
}
