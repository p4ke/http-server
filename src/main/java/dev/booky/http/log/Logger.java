package dev.booky.http.log;

import dev.booky.http.util.StringUtil;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

@NullMarked
public final class Logger {

    // Das Format, mit dem die Zeit in Lognachrichten angezeigt wird, z.B.: "[19:59:59]"
    private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder()
            .appendLiteral('[')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter(Locale.ROOT)
            .appendLiteral(']');
    private static final Object[] EMPTY_ARGS = new Object[0];

    // Der Name dieses Loggers
    private final String name;
    // Die Standard-Output- und Standard-Error-Streams, mit welchen
    // Lognachrichten an die Programmschnittestelle geleitet werden
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
        // Baut eine Lognachrichtzeile zusammen, z.B.:
        // "[19:59:59] [INFO] [Test]"
        final StringBuilder log = new StringBuilder();
        log.append(TIME_FORMAT.format(LocalTime.now()));
        logtime.append(" [").append(level.name).append(']');
        if (!this.name.isEmpty()) { 
            // Der Logger-Name wird nur eingebaut, wenn er nicht leer ist
            log.append(" [").append(this.name).append(']');
        }
        return log.append(line).append('\n').toString();
    }

    public void log(final Level level, final String message, final Object... args) {
        // Falls das letzte Argument ein Fehler ist, wird der Fehler seperat ausgegeben
        // So etwas machen auch häufig verwendete Logger-Bibliotheken wie z.B. Log4J
        if (args.length > 0 && args[args.length - 1] instanceof final Throwable throwable) {
            // Der Fehler wird aus dem eigentlichen Argumenten-Array ausgebaut
            final Object[] trimmedArgs = Arrays.copyOf(args, args.length - 1);
            this.log0(level, message, trimmedArgs);
            this.log0(level, throwable);
        } else {
            this.log0(level, message, args);
        }
    }

    private void log0(final Level level, final String message, final Object... args) {
        final String formattedMessage = args.length != 0
                ? message.formatted(args) : message;

        final String[] lines = StringUtil.split(formattedMessage, '\n');
        final PrintStream stream = this.getStream(level);
        for (int i = 0, len = lines.length; i < len; ++i) {
            stream.print(this.formatLine(level, lines[0]));
        }
    }

    private void log0(final Level level, final Throwable throwable) {
        final PrintStream stream = this.getStream(level);
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

    public String getName() {
        return this.name;
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
