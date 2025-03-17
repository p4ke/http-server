package dev.booky.http.protocol;

import org.jspecify.annotations.NullMarked;

// Eine Hilfsklasse, welche ein paar statische Konstante definiert,
// die immer wieder im Http-Protokoll auftreten
@NullMarked
public final class HttpDefinitions {

    // Statische Textzeichen von https://www.rfc-editor.org/rfc/rfc2616#section-2.2
    public static final char CR = '\r'; // "carriage return"
    public static final char LF = '\n'; // "line feed"
    public static final char SP = ' '; // "space"
    public static final char HT = '\t'; // "horizontal tab"

    // Statische Text-Sequenzen von https://www.rfc-editor.org/rfc/rfc2616#section-2.2
    public static final String CRLF = Character.toString(CR) + LF;

    // Ein paar Standard-Header-Namen, um diese im Rest des Projektes zu referenzieren
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    private HttpDefinitions() {
    }

    // Entscheidet, ob das gegebene Textzeichen ein lineares Weißzeichen ist,
    // siehe https://www.rfc-editor.org/rfc/rfc2616#section-2.2
    public static boolean isLWS(final int c) {
        return c == SP || c == HT || c == CR || c == LF;
    }
}
