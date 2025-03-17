package dev.booky.http.log;

import org.jspecify.annotations.NullMarked;

import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Eine "Fabrik", welche eine Liste an bereits existierenden Loggern
// und die Standard Input- und Error-Streams beinhaltet
@NullMarked
public final class LoggerFactory {

    private static final PrintStream STOUT = System.out;
    private static final PrintStream STERR = System.err;

    // Dies ist eine "ConcurrentHashMap" und keine normale "HashMap", da diese Logger
    // teilweise auch parallel geladen werden könnten und so dadurch keine Fehler entstehen
    private static final Map<String, Logger> LOGGERS = new ConcurrentHashMap<>();

    private LoggerFactory() {
    }

    public static Logger getLogger(final String name) {
        // Falls ein Logger mit dem gegebenen Namen noch nicht existiert,
        // wird er neu konstruiert
        return LOGGERS.computeIfAbsent(name, LoggerFactory::constructLogger);
    }

    private static Logger constructLogger(final String name) {
        // Konstruiert ein neues Logger-Objekt, basierend auf dem Namen
        return new Logger(name, STOUT, STERR);
    }
}
