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
        final HttpReader reader = new HttpReader(""
                + "GET /pub/WWW/TheProject.html HTTP/1.1\r\n"
                + "Host: www.w3.org\r\n"
                + "\r\n"
                + "Message body\r\n"
                + "jsdnfkjdfgnkjsdf");
        final HttpMessage message = HttpMessage.parseMessage(reader);
        LOGGER.info(message.getMethod().toString());
        LOGGER.info(message.getUri().toString());
        LOGGER.info(message.getVersion().toString());
        LOGGER.info(message.getHeaders().toString());
    }
}
