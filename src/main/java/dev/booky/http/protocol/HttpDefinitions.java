package dev.booky.http.protocol;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class HttpDefinitions {

    // static characters from https://www.rfc-editor.org/rfc/rfc2616#section-2.2
    public static final char CR = '\r'; // carriage return
    public static final char LF = '\n'; // line feed
    public static final char SP = ' '; // space
    public static final char HT = '\t'; // horizontal tab
    public static final char QUOTE = '"'; // double quote mark

    // static text sequences from https://www.rfc-editor.org/rfc/rfc2616#section-2.2
    public static final String CRLF = Character.toString(CR) + LF;

    // utility constants
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private HttpDefinitions() {
    }

    public static boolean isLWS(final char c) {
        // defined by https://www.rfc-editor.org/rfc/rfc2616#section-2.2
        return c == SP || c == HT || c == CR || c == LF;
    }
}
