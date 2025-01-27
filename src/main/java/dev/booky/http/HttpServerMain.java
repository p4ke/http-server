package dev.booky.http;

import dev.booky.http.log.Logger;
import dev.booky.http.log.LoggerFactory;
import dev.booky.http.protocol.HttpMessage;
import dev.booky.http.util.HttpReader;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HttpServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger("Main");

    public static void main(final String[] args) {
        LOGGER.info("Hello %s", "World");
        final HttpReader reader = new HttpReader("GET X-Forwarded-For:      1.1.1.1      \r\n  test");
        final HttpMessage message = HttpMessage.parseMessage(reader);
        System.out.println(message.getMethod());
        System.out.println(message.getHeaders());
    }
}
