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
        // Ruft gemeinsame Logik auf, um die Zeichenkette je nach Zeichenanzahl zu konstruieren
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
        // Ruft gemeinsame Logik auf, um die Zeichenkette je nach Zeichenanzahl zu konstruieren
        return this.readLineUntil0(c, charCount);
    }

    // Hier wird die aktuelle Zeile bis zum Ende des Zeilenumbruchs gelesen;
    // ein wichtiges Merkmal ist, dass der Zeilenumbruch auch noch "konsumiert" wird,
    // aber nicht in der zurückgegebenen Zeichenkette auftaucht
    public String readSingleLine() throws IOException {
        // Zuerst wird die aktuelle Position markiert
        this.reader.mark(MAX_LINE_LENGTH);
        // Auch hier wird wieder zuerst gezählt, wie viele Zeichen gelesen werden
        // müssen, dann zurückgesetzt, und dann wieder gelesen
        int charCount = 0;
        // Da im Http-Protokoll alle Zeilenumbrüche im Format "CR" "LF" sind,
        // wird hier abgespeichert, ob das erste dieser Zeichen (Wagenrücklauf-Zeichen) gelesen wurde
        boolean endOfLineTrigger = false;
        int c;
        while ((c = this.reader.read()) != -1) {
            // Falls ein Wagenrücklaufs-Zeichen gelesen wurde, wird nun ein Zeilenvorschubs-Zeichen erwartet
            if (endOfLineTrigger) {
                if (c == LF) {
                    // Der Zeilenumbruch ist vollständig und die Zeile ist damit fertig gelesen;
                    // die Schleife wird somit beendet
                    break;
                }
                // Es wird ein Fehler geworfen
                throw new IllegalStateException("Expected LF character after CR character; received '" + ((char) c) + "'");
            } else if (c == CR) {
                // Da ein Wagenrücklaufs-Zeichen gelesen wurde, wird nun ein Zeilenvorschubs-Zeichen erwartet
                endOfLineTrigger = true;
            } else if (c == LF) {
                // Bevor ein Zeilenvorschubs-Zeichen gelesen wird, sollte laut Http-Protokoll
                // immer ein Wagenrücklaufs-Zeichen davor stehen
                throw new IllegalStateException("Encountered LF character without CR character");
            } else {
                // Weder Wagenrücklauf noch Zeilenvorschub, ein ganz normales Zeichen
                ++charCount;
            }
        }
        // Ruft gemeinsame Logik auf, um die Zeichenkette je nach Zeichenanzahl zu konstruieren
        return this.readLineUntil0(c, charCount);
    }

    // Gemeinsame Logik von "HttpReader#readLineUntilLWS()", "HttpReader#readLineUntil(char)" und "HttpReader#readSingleLine()",
    // wo basierend auf gerade ausgelesenen Zuständen eine bestimmte Zeichenkette gelesen wird
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

    // Hier werden alle folgenden Weißzeichen übersprungen, falls vorhanden
    // und gibt zurück, ob mindestens ein Weißzeichen übersprungen wurde
    public boolean skipLWS() throws IOException {
        // Zuerst wird die aktuelle Position markiert - da erstmal nur ein Zeichen gelesen
        // wird, ist das "read-ahead-limit" auf eins gesetzt
        this.reader.mark(1);
        // Da diese Methode einen Wert zurückgibt, wird
        // hier abgespeichert, ob mindestens ein Weißzeichen gefunden wurde
        boolean ret = false;
        // Erstmal wird so lange gelesen, bis das Ende des Readers erreicht wurde
        int c;
        while ((c = this.reader.read()) != -1) {
            // Falls das soeben gelesene Zeichen ein Weißzeichen ist,
            // wird diese Schleife abgebrochen
            if (!HttpDefinitions.isLWS(c)) {
                break;
            }
            // Es wurde ein Weißzeichen gefunden! Dieser Status wird abgespeichert
            ret = true;
            // Der Reader wird wieder markiert, um das finale Zurücksetzen zu gewährleisten
            this.reader.mark(1);
        }
        if (c != -1) {
            // Falls das Ende des Readers noch nicht erreicht wurde,
            // wird ein Zeichen zurückgesetzt, da ansonsten ein nicht-Weißzeichen
            // übersprungen wurde
            this.reader.reset();
        }
        return ret;
    }

    // Hier wird ein mehrzeiliger Http-Wert gelesen - dies
    // wird aktuell nur bei Http-Header-Werten benötigt
    public String readMultiLine() throws IOException {
        final StringBuilder builder = new StringBuilder();
        // Es wird mit einer fußgesteuerten While-Schleife immer eine Zeile
        // nach der anderen gelesen, solange die nächste Zeile mit Weißzeichen beginnt
        do {
            // Hier wird eine Zeile gelesen und nachfolgende Weißzeichen
            // mit Java-Standard-Methoden entfernt
            final String line = this.readSingleLine().stripTrailing();
            // Die Zeile wird abgespeichert und erstmal mit einem einzelnen Leerzeichen
            builder.append(line).append(SP);
            // Da "HttpReader#readSingleLine()" auch das Zeilenumbruchs-Zeichen
            // konsumiert, wird hier in der Bedingung überprüft, dass keine zwei Zeilenumbrüche
            // hintereinander auftreten
            // Zudem wird überprüft, dass die darauffolgende Zeile mit einem Weißzeichen anfängt - falls das
            // nicht der Fall ist, ist das auch das Ende des Multi-Zeilen-Textes
        } while (this.isReadable(CRLF.length()) // Überprüfung, dass noch genug Platz für einen Zeilenumbruch ist
                && !CRLF.equals(this.peek(CRLF.length())) // Falls ein Zeilenumbruch folgt, wird abgebrochen
                && this.skipLWS()); // Falls KEIN Weißzeichen am Start der nächsten Zeile ist, wird auch abgebrochen
        // Schließlich wird der Zeichenketten-Bauer in eine Zeichenkette umgewandelt
        // und das Leerzeichen, was vorher immer am Ende angefügt wurde, wird
        // ganz am Ende des Multi-Zeilen-Textes entfernt
        return builder.toString().stripTrailing();
    }

    // Falls möglich, wird ein Zeilenumbruch übersprungen; ob
    // dies klappt oder nicht, wird als Rückgabewert übergeben
    public boolean skipCRLF() throws IOException {
        // Falls mindestens ein Zeilenumbruch gelesen werden kann
        // und die nächsten zwei Zeichen (länge eines Zeilenumbruchs) auch
        // wirklich ein Zeilenumbruch ist, ...
        if (this.isReadable(CRLF.length())
                || CRLF.equals(this.peek(CRLF.length()))) {
            // ... wird dieser Zeilenumbruch übersprungen und ein Erfolg zurückgegeben; ...
            this.skip(CRLF.length());
            return true;
        }
        // ... falls kein Zeilenumbruch folgt, wird kein Erfolg zurückgegeben
        return false;
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
