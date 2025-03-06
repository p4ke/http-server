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

    // Teilt die Zeichenkette bei dem gegebenen Trenner, ohne den Trenner als Regex zu betrachten,
    // wie die Standard-Java-Methode "String#split(String)" es würde
    public static String[] split(final String string, final char separator) {
        int index = string.indexOf(separator);
        if (index < 0) {
            // Falls das Trennzeichen nie in der Zeichenkette auftaucht, wird
            // direkt die Ursprungszeichenkette zurückgegeben
            return new String[]{string};
        }

        // Nun wird in der Zeichenkette immer weiter das Trennzeichen gesucht
        // und alle Einzelteile werden abgespeichert
        final List<String> parts = new ArrayList<>();
        int lastIndex = -1;
        do {
            parts.add(string.substring(lastIndex + 1, index));
            lastIndex = index;
            index = string.indexOf(separator, index + 1);
        } while (index >= 0);

        // Schließlich werden alle Einzelteile als Array ausgegeben
        parts.add(string.substring(lastIndex + 1));
        return parts.toArray(new String[0]);
    }

    public static String stringifyBindAddress(final SocketAddress address) {
        // Da unter Windows "0.0.0.0" nicht als lokale Adresse erreichbar ist, wird
        // durch diese Logik "0.0.0.0" durch "127.0.0.1" ersetzt
        if (address instanceof final InetSocketAddress inetAddress
                && inetAddress.getAddress().isAnyLocalAddress()) {
            return stringifyAddress(new InetSocketAddress("127.0.0.1", inetAddress.getPort()));
        }
        return stringifyAddress(address);
    }

    // Hier wird eine Adresse zu einem String konvertiert
    public static String stringifyAddress(final SocketAddress address) {
        if (!(address instanceof final InetSocketAddress inetAddress)) {
            // Falls es keine IP-Adresse ist, wird
            // hier keine spezielle Logik angewandt
            return address.toString();
        }
        // Die Adresse und der Port wird zu einem String konvertiert
        final String addressStr = inetAddress.getAddress().getHostAddress();
        final String portStr = Integer.toString(inetAddress.getPort());

        // Bei IPv6-Socket-Adressen muss die Adresse in eckigen Klammern gesetzt
        // werden, damit Anwendungen den Port und die Adresse unterscheiden können
        if (inetAddress.getAddress() instanceof Inet6Address) {
            return '[' + addressStr + "]:" + portStr;
        }
        return addressStr + ':' + portStr;
    }
}
