package dev.booky.http.util;

import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class StringUtil {

    private StringUtil() {
    }

    private static boolean isQuote(final char c) {
        return c == '\'' || c == '"';
    }

    public static String removeQuoting(final String string) {
        if (string.length() < 2) {
            return string; // no quoting possible
        }
        // determine whether the string has quoting at the start and quoting at the end
        final boolean startQuote = isQuote(string.charAt(0));
        final boolean endQuote = isQuote(string.charAt(string.length() - 1));
        if (!startQuote && !endQuote) {
            return string; // no quoting present
        }
        // remove quotes at start and quotes at the end of the string, if present
        return string.substring(startQuote ? 1 : 0, string.length() - (endQuote ? 1 : 0));
    }

    /**
     * Splits the specified string without regex parsing
     */
    public static String[] split(final String string, final char separator) {
        int index = string.indexOf(separator);
        if (index < 0) { // fast path
            return new String[]{string};
        }

        final List<String> parts = new ArrayList<>();
        int lastIndex = -1;
        do {
            parts.add(string.substring(lastIndex + 1, index));
            lastIndex = index;
            index = string.indexOf(separator, index + 1);
        } while (index >= 0);

        parts.add(string.substring(lastIndex + 1));
        return parts.toArray(new String[0]);
    }

    public static String stringifyAddress(final SocketAddress address) {
        if (!(address instanceof final InetSocketAddress inetAddress)) {
            return address.toString();
        }
        final String addressStr = inetAddress.getAddress().getHostAddress();
        final String portStr = Integer.toString(inetAddress.getPort());

        if (inetAddress.getAddress() instanceof Inet6Address) {
            return '[' + addressStr + "]:" + portStr;
        }
        return addressStr + ':' + portStr;
    }
}
