package dev.booky.http.log;

import dev.booky.http.util.StringUtil;
import org.jspecify.annotations.NullMarked;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Locale;

@NullMarked
public final class Logger {

    // Das Format, mit dem die Zeit in Lognachrichten angezeigt wird, z.B.: "19:59:59"
    private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter(Locale.ROOT);

    // Der Name dieses Loggers
    private final String name;
    // Die Standard-Output- und Standard-Error-Streams, mit welchen
    // Nachrichten an die Programmschnittestelle geleitet werden
    private final PrintStream stout;
    private final PrintStream sterr;

    public Logger(
            final String name,
            final PrintStream stout,
            final PrintStream sterr
    ) {
        this.name = name;
        this.stout = stout;
        this.sterr = sterr;
    }

    private String formatLine(final Level level, final String line) {
        // Baut eine Log-Zeile zusammen, z.B. "[19:59:59 INFO] [Test] Hallo Welt!"
        return '[' + TIME_FORMAT.format(LocalTime.now())
                + ' ' + level.name + "] [" + this.name + "] " +
                // Schließlich wird die eigentliche Nachricht an die Meta-Infos angehängt
                line + '\n';
    }

    public void log(final Level level, final String message, final Object... args) {
        // Falls das letzte Argument ein Fehler ist, wird der Fehler separat ausgegeben
        // So etwas machen auch häufig verwendete Logger-Bibliotheken wie z.B. Log4J
        if (args.length > 0 && args[args.length - 1] instanceof final Throwable throwable) {
            // Der Fehler wird aus dem eigentlichen Argumenten-Array ausgebaut
            final Object[] trimmedArgs = Arrays.copyOf(args, args.length - 1);
            this.logDirect(level, message, trimmedArgs);
            this.logError(level, throwable);
        } else {
            this.logDirect(level, message, args);
        }
    }

    private void logDirect(final Level level, final String message, final Object... args) {
        // Basierend auf dem angegebenen Log-Level wird ein Output-Stream ausgesucht
        final PrintStream stream = this.getStream(level);

        // Falls es Argumente gibt, werden diese in der angegebenen Nachricht ersetzt
        final String formattedMessage = args.length != 0
                ? message.formatted(args) : message;
        // Die formatierte Nachricht wird an Zeilenumbrüchen getrennt, damit
        // jede einzelne Zeile vernünftig formatiert werden kann
        final String[] lines = StringUtil.split(formattedMessage, '\n');
        for (int i = 0, len = lines.length; i < len; ++i) {
            // Nun wird jede einzelne Zeile formatiert ...
            final String formattedLine = this.formatLine(level, lines[0]);
            // ... und an den Output-Stream ausgegeben, um sie in der Konsole anzuzeigen
            stream.print(formattedLine);
        }
    }

    private void logError(final Level level, final Throwable throwable) {
        // Basierend auf dem angegebenen Log-Level wird ein Output-Stream ausgesucht
        final PrintStream stream = this.getStream(level);
        // Der komplette Error wird einfach an den Output-Stream weitergegeben
        throwable.printStackTrace(stream);
    }

    private PrintStream getStream(final Level level) {
        // Löst den jeweiligen Output-Stream aus einem Log-Level auf
        return switch (level.stream) {
            case OUT -> this.stout;
            case ERR -> this.sterr;
        };
    }

    public void info(final String message, final Object... args) {
        this.log(Level.INFO, message, args);
    }

    public void warn(final String message, final Object... args) {
        this.log(Level.WARN, message, args);
    }

    public void error(final String message, final Object... args) {
        this.log(Level.ERROR, message, args);
    }

    // Mögliche Standard-Output-Streams
    private enum StandardStream {
        OUT,
        ERR,
    }

    // Mögliche Log-Level
    public enum Level {

        INFO(StandardStream.OUT, "INFO"),
        WARN(StandardStream.ERR, "WARN"),
        ERROR(StandardStream.ERR, "ERROR");

        private final StandardStream stream;
        private final String name;

        Level(final StandardStream stream, final String name) {
            this.stream = stream;
            this.name = name;
        }
    }
}
