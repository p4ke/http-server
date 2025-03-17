package dev.booky.http.util;

import dev.booky.http.protocol.HttpDefinitions;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.Reader;

import static dev.booky.http.protocol.HttpDefinitions.CR;
import static dev.booky.http.protocol.HttpDefinitions.CRLF;
import static dev.booky.http.protocol.HttpDefinitions.LF;
import static dev.booky.http.protocol.HttpDefinitions.SP;

// Einer der Hauptbestandteile dieses Projekts, da hier
// aus einem Text-Reader der Java-Standard-Bibliothek
// (mit mark-/reset-Unterstützung) bestimmte mehrfach verwendeten
// String-Strukturen ausgelesen werden können
@NullMarked
public final class HttpReader {

    // Bestimmte Konstante, siehe deren Verwendung für mehr Informationen
    private static final int REMAINING_TEXT_CHAR_BUFFER_SIZE = 8192;
    private static final int MAX_LINE_LENGTH = 256;

    // Der eigentliche Reader (mit mark-/reset-Unterstützung)
    private final Reader reader;

    public HttpReader(final Reader reader) {
        this.reader = reader;
    }

    // Liest die aktuelle Zeile bis ein Weißzeichen auftritt
    public String readLineUntilLWS() throws IOException {
        // Zuerst wird die aktuelle Position markiert
        this.reader.mark(MAX_LINE_LENGTH);
        // Nun wird der Reader so lange gelesen, bis ein Weißzeichen auftritt;
        // zudem wird gezählt, wie viele Zeichen bis zu dem Weißzeichen gelesen werden konnten
        int charCount = 0;
        int c;
        while ((c = this.reader.read()) != -1 && !HttpDefinitions.isLWS(c)) {
            ++charCount;
        }
        // Ruft gemeinsame Logik mit der Methode "HttpReader#readLineUntil(char)" auf
        return this.readLineUntil0(c, charCount);
    }

    // Liest die aktuelle Zeile, bis ein bestimmtes Zeichen auftritt;
    // falls die Zeile (oder der ganze Reader) vorzeitig endet, wird ein Fehler geworfen
    public String readLineUntil(final char searchChar) throws IOException {
        // Zuerst wird die aktuelle Position markiert
        this.reader.mark(MAX_LINE_LENGTH);
        // Nun wird der Reader so lange gelesen, bis das gesuchte Zeichen auftritt;
        // zudem wird gezählt, wie viele Zeichen bis zu dem Weißzeichen gelesen werden konnten
        int charCount = 0;
        int c;
        while ((c = this.reader.read()) != -1 && c != searchChar) {
            // Überprüft zusätzlich, dass nicht das Ende einer Zeile erreicht wurde
            if (c == CR || c == LF) {
                // Es wurde das Ende einer Zeile erreicht - der Reader wird
                // zurück an die markierte Position gesetzt und es wird ein Fehler geworfen
                this.reader.reset();
                throw new IllegalArgumentException("Line ends before next occurrence of '"
                        + c + "' in remaining string: '" + this.getRemaining() + "'");
            }
            ++charCount;
        }
        // Ruft gemeinsame Logik mit der Methode "HttpReader#readLineUntilLWS()" auf
        return this.readLineUntil0(c, charCount);
    }

    private String readLineUntil0(final int lastChar, final int charCount) throws IOException {
        // Entweder wurde das Zeichen gefunden oder der Reader hat das Ende erreicht -
        // auf jeden Fall wird erstmal der Reader wieder zurück an die markierte Position gesetzt
        this.reader.reset();
        // Falls tatsächlich der Reader das Ende erreicht hat, wird eine Fehlermeldung ausgegeben -
        // diese Methode erwartet, dass das gesuchte Zeichen auf jeden Fall auftreten wird
        if (lastChar == -1) {
            throw new IllegalStateException("Can't find character in remaining string: '" + this.getRemaining() + "'");
        }
        // Schließlich wird ein Array an Zeichen erstellt, basierend darauf, wie viele
        // vorher bis zum gesuchten Zeichen gelesen werden konnten
        final char[] chars = new char[charCount];
        // Nun wird das soeben erstelle Zeichen-Array mit dem Inhalt des Readers gefüllt
        final int readChars = this.reader.read(chars);
        // Hier wird sich vergewissert, dass der Reader tatsächlich die volle gezählte Länge
        // der Zeichenkette eingelesen hat; dies sollte nie fehlschlagen
        assert readChars == charCount;
        // Schließlich wird ein Java Zeichenketten-Objekt mithilfe des
        // eingelesenen Zeichen-Arrays erstellt
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
