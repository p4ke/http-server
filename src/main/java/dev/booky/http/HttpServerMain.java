package dev.booky.http;

import dev.booky.http.log.Logger;
import dev.booky.http.log.LoggerFactory;
import dev.booky.http.protocol.HttpHeaders;
import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HttpServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger("Main");

    public static void main(final String[] args) {
        LOGGER.info("Hello %s", "World");
        HttpReader reader = new HttpReader("X-Forwarded-For:      1.1.1.1\r\n  test");
        System.out.println(HttpHeaders.headersParsed(reader).getRawHeaders());
    }
}
