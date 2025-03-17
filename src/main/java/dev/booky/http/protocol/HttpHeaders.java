package dev.booky.http.protocol;

import dev.booky.http.HttpServer;
import dev.booky.http.util.HttpReader;
import dev.booky.http.util.StringUtil;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.booky.http.protocol.HttpDefinitions.CRLF;

// Beinhaltet eine Map an Http-Header-Namen zu Http-Header-Werten
@NullMarked
public final class HttpHeaders {

    // Siehe https://www.rfc-editor.org/rfc/rfc2616#section-14.18
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
                    "EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            .withZone(ZoneId.of("GMT"));

    private final Map<String, String> headers;

    private HttpHeaders(final Map<String, String> headers) {
        this.headers = headers.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> Map.entry(normalizeHeaderName(entry.getKey()), entry.getValue()))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // "Normt" den Namen eines Http-Headers
    private static String normalizeHeaderName(final String name) {
        // Für Header-Namen ist Groß-/Kleinschreibung egal, siehe https://www.rfc-editor.org/rfc/rfc2616#section-4.2
        return name.toLowerCase(Locale.ROOT);
    }

    // Fügt zwei Header-Werte zusammen, nach den Regeln von https://www.rfc-editor.org/rfc/rfc2616#section-4.2
    @Contract("null, null -> null; !null, _ -> !null; _, !null -> !null")
    private static @Nullable String joinHeaderValues(@Nullable final String value1, @Nullable final String value2) {
        if (value1 == null) return value2;
        if (value2 == null) return value1;
        return value1 + ',' + value2;
    }

    public static HttpHeaders buildResponseHeaders(final Map<String, String> headers) {
        final Map<String, String> allHeaders = new HashMap<>();
        for (final Map.Entry<String, String> entry : headers.entrySet()) {
            // Um Dopplung bei den Antwort-Headern zu vermeiden,
            // werden hier die Header-Namen normalisiert
            allHeaders.put(normalizeHeaderName(entry.getKey()), entry.getValue());
        }
        // Falls nicht bereits gesetzt, werden hier einige Standard-Header gesetzt
        allHeaders.putIfAbsent("server", HttpServer.class.getSimpleName());
        allHeaders.putIfAbsent("date", DATE_FORMAT.format(ZonedDateTime.now()));
        return new HttpHeaders(allHeaders);
    }

    // Nutzt den HTTP-Reader, um alle HTTP-Header auszulesen, bis entweder der
    // Inhalt vorbei ist oder der Anfragen-Inhalt anfängt
    public static HttpHeaders parseHeaders(final HttpReader reader) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        do {
            if (CRLF.equals(reader.peek(CRLF.length()))) {
                // Falls nach einem Zeilenbruch (wird in der Bedingung der Fußgesteuerten
                // while-Schleife überprüft) noch ein weiterer Zeilenbruch vorliegt,
                // ist das Ende der HTTP-Header erreicht
                break;
            }
            // Ein Header ist normalerweise nach "<Name> ': ' <Wert>" formatiert -
            // deshalb wird jetzt bis zum ersten ":" der Header-Name eingelesen
            final String name = reader.readLineUntil(':');
            // Es wird das ":" übersprungen, sowie jede weiteren Weißzeichen
            reader.skip().skipLWS();
            // Der Wert des Headers kann auch mehrzeilig sein - es wird nun bis zum nächsten
            // Zeilenbruch ohne Fortsetzung des Headers der Wert des Headers eingelesen
            final String value = reader.readMultiLine();
            // Schließlich wird der Header-Wert in einer Map abgespeichert - falls der Header-Wert bereits
            // existiert, werden die Werte nach Http-Standard zusammengefügt
            headers.compute(normalizeHeaderName(name),
                    (key, existingValue) -> joinHeaderValues(existingValue, value));
            // Nachdem der eine Header gelesen wurde, wird ein Zeilensprung erwartet - falls
            // dieser vorliegt und noch mindestens ein weiterer Zeilensprung gelesen werden könnte,
            // wird weiter gelesen
        } while (reader.skipCRLF() && reader.isReadable(CRLF.length()));
        // Aus der eingelesenen Map wird nun ein Objekt gebaut
        return new HttpHeaders(headers);
    }

    public void writeTo(final BufferedWriter writer) throws IOException {
        // Es wird über alle Header-Einträge iteriert
        for (final Map.Entry<String, String> entry : this.headers.entrySet()) {
            // Ein Header ist normalerweise nach "<Name> ': ' <Wert>" formatiert -
            // deshalb wird zuerst der Header-Name geschriebene ...
            writer.write(entry.getKey());
            // ... dann das Trennzeichen ...
            writer.write(": ");
            // ... und schließlich der Wert des Headers
            writer.write(entry.getValue());

            // Am Ende wird das Ende des Header-Einträgs mithilfe
            // eines Zeilensprungs markiert
            writer.write(CRLF);
        }
    }

    public @Nullable String getHeader(final String name) {
        return this.headers.get(normalizeHeaderName(name));
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public String toString() {
        return this.headers.toString();
    }
}
