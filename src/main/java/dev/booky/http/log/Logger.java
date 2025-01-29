package dev.booky.http.log;

import dev.booky.http.util.StringUtil;
import org.jspecify.annotations.NullMarked;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

@NullMarked
public class Logger {

    private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder()
            .appendLiteral('[')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter(Locale.ROOT);
    private static final Object[] EMPTY_ARGS = new Object[0];

    private final String name;
    private final String formattedName;

    private final PrintStream stout;
    private final PrintStream sterr;

    public Logger(final String name, final PrintStream stout, final PrintStream sterr) {
        this.name = name;
        this.formattedName = !name.isEmpty() ? "[%s] ".formatted(name) : "";

        this.stout = stout;
        this.sterr = sterr;
    }

    private String formatLine(final Level level, final String line) {
        final String time = TIME_FORMAT.format(LocalTime.now());
        return time + level.formattedName + this.formattedName + line + '\n';
    }

    public void log(final Level level, final String message, final Object... args) {
        final PrintStream stream = switch (level.stream) {
            case OUT -> this.stout;
            case ERR -> this.sterr;
        };
        final String formattedMessage = args.length != 0
                ? message.formatted(args) : message;

        final String[] lines = StringUtil.split(formattedMessage, '\n');
        for (int i = 0, len = lines.length; i < len; ++i) {
            stream.print(this.formatLine(level, lines[0]));
        }
    }

    public void log(final Level level, final String message) {
        this.log(level, message, EMPTY_ARGS);
    }

    public void info(final String message, final Object... args) {
        this.log(Level.INFO, message, args);
    }

    public void info(final String message) {
        this.info(message, EMPTY_ARGS);
    }

    public void warn(final String message, final Object... args) {
        this.log(Level.WARN, message, args);
    }

    public void warn(final String message) {
        this.warn(message, EMPTY_ARGS);
    }

    public void error(final String message, final Object... args) {
        this.log(Level.ERROR, message, args);
    }

    public void error(final String message) {
        this.error(message, EMPTY_ARGS);
    }

    public String getName() {
        return name;
    }

    private enum StandardStream {
        OUT,
        ERR,
    }

    public enum Level {

        INFO(StandardStream.OUT, "INFO"),
        WARN(StandardStream.ERR, "WARN"),
        ERROR(StandardStream.ERR, "ERROR");

        private final StandardStream stream;
        private final String formattedName;

        Level(final StandardStream stream, final String name) {
            this.stream = stream;
            this.formattedName = " %s] ".formatted(name);
        }
    }
}
