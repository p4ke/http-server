package dev.booky.http.util;

import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class SplitUtil {

    private SplitUtil() {
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
}
