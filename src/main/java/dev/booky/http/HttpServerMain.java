package dev.booky.http;

import dev.booky.http.log.LoggerFactory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HttpServerMain {

    public static void main(final String[] args) {
        LoggerFactory.getLogger().info("Hello %s", "World");
    }
}
