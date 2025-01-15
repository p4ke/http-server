package dev.booky.http.util;

import dev.booky.http.protocol.HttpDefinitions;
import org.jspecify.annotations.NullMarked;

import static dev.booky.http.protocol.HttpDefinitions.*;

@NullMarked
public final class HttpReader {

    private final String data;
    private int cursor = 0;

    public HttpReader(final String data) {
        this.data = data;
    }

    public String readLineUntil(final char c) {
        final int charIndex = this.data.indexOf(c, this.cursor);
        if (charIndex < 0) {
            throw new IllegalArgumentException("Can't find character '"
                    + c + "' in remaining string: '" + this.getRemaining() + "'");
        } else if (this.data.indexOf(CRLF, this.cursor) < charIndex) {
            throw new IllegalArgumentException("Line ends before next occurrence of '"
                    + c + "' in remaining string: '" + this.getRemaining() + "'");
        }
        final int prevCursor = this.cursor;
        this.cursor = charIndex;
        return this.data.substring(prevCursor, charIndex + 1);
    }

    public boolean skipLWS() {
        boolean ret = false;
        while (HttpDefinitions.isLWS(this.peek())) {
            this.skip();
            ret = true;
        }
        return ret;
    }

    public String readMultiLine() {
        final StringBuilder builder = new StringBuilder();
        do {
            builder.append(this.readSingleLine());
        } while (this.isReadable() && this.skipLWS());
        return builder.toString();
    }

    public String readSingleLine() {
        int lineIndex = this.data.indexOf(CRLF, this.cursor);
        if (lineIndex < 0) lineIndex = this.data.length();
        final int prevCursor = this.cursor;
        this.cursor = lineIndex;
        return this.data.substring(prevCursor, lineIndex);
    }

    public boolean skipCRLF() {
        if (!this.isReadable(CRLF.length())
                || this.data.charAt(this.cursor) != CR
                || this.data.charAt(this.cursor + 1) != LF) {
            return false;
        }
        this.skip(CRLF.length());
        return true;
    }

    public char next() {
        return this.data.charAt(this.cursor++);
    }

    public char peek() {
        return this.data.charAt(this.cursor);
    }

    public HttpReader skip() {
        ++this.cursor;
        return this;
    }

    public HttpReader skip(final int amount) {
        this.cursor += amount;
        return this;
    }

    public boolean isReadable(final int amount) {
        return this.cursor <= this.data.length() - amount;
    }

    public boolean isReadable() {
        return this.cursor < this.data.length();
    }

    public String getRemaining() {
        return this.data.substring(this.cursor);
    }

    public int getCursor() {
        return this.cursor;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }
}
