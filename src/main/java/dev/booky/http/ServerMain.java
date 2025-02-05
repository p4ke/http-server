package dev.booky.http;

import dev.booky.http.HttpServer.ServerParameters;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

@NullMarked
public class ServerMain {

    public static void main(final String[] args) throws IOException, InterruptedException {
        final InetSocketAddress address = new InetSocketAddress("0.0.0.0", 49275);
        try (final HttpServer server = HttpServer.create(address, new ServerParameters(Path.of("demo")))) {
            server.wait();
        }
    }
}
