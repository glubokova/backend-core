package ru.mentee.power.crm.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HelloCrmServer {
    private final HttpServer server;
    private final int port;

    public HelloCrmServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public void start() {
        server.createContext("/hello", new HelloHandler());
        server.start();
        System.out.println("Server started on http://localhost:" + port);
        System.out.println("Try: http://localhost:" + port + "/hello");

    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws  IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("Received " + method + " request for " + path);

            String html = """
                    <html>
                        <head>
                            <meta charset="UTF-8">
                            <title>Hello CRM</title>
                        </head>
                        <body>
                            <h1>Hello CRM!</h1>
                        </body>
                    </html>
                    """;

            byte[] responseBytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200,responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}