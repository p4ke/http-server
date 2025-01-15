package dev.booky.http.log;

import org.jspecify.annotations.NullMarked;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class LoggerFactory {

    private static final PrintStream STOUT = System.out;
    private static final PrintStream STERR = System.err;
    private static final String BLANK_LOGGER = "";

    private static final Map<String, Logger> LOGGERS = new ConcurrentHashMap<>();

    private LoggerFactory() {
    }

    public static Logger getLogger(final Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger() {
        return getLogger(BLANK_LOGGER);
    }

    public static Logger getLogger(final String name) {
        return LOGGERS.computeIfAbsent(name, LoggerFactory::constructLogger);
    }

    private static Logger constructLogger(final String name) {
        return new Logger(name, STOUT, STERR);
    }
}
