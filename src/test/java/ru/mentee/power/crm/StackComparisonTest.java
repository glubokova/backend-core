package ru.mentee.power.crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class StackComparisonTest {

    private static final int SERVLET_PORT = 8080;
    private static final int SPRING_PORT = 8081;

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("Оба стека должны возвращать одинаковые данные")
    void shouldReturnLeadsFromBothStacks() throws Exception {

        HttpRequest servletRequest = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + SERVLET_PORT + "/leads"))
                .GET()
                .build();

        HttpRequest springRequest = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + SPRING_PORT + "/leads"))
                .GET()
                .build();

        HttpResponse<String> servletResponse =
                httpClient.send(
                        servletRequest,
                        HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> springResponse =
                httpClient.send(
                        springRequest,
                        HttpResponse.BodyHandlers.ofString());

        assertThat(servletResponse.statusCode()).isEqualTo(200);
        assertThat(springResponse.statusCode()).isEqualTo(200);

        assertThat(servletResponse.body()).contains("<table");
        assertThat(springResponse.body()).contains("<table");

        int servletRows = countTableRows(servletResponse.body());
        int springRows = countTableRows(springResponse.body());

        System.out.println("Servlet rows: " + servletRows);
        System.out.println("Spring rows: " + springRows);

        assertThat(servletRows)
                .as("Количество строк <tr> должно совпадать")
                .isEqualTo(springRows);
    }

    @Test
    @DisplayName("Сравнение времени ответа")
    void shouldMeasureStartupTime() throws Exception {

        long servletTime = measureServletStartup();
        long springTime = measureSpringBootStartup();

        assertThat(servletTime).isPositive();
        assertThat(springTime).isPositive();

        System.out.println(
                "Servlet response time: "
                        + servletTime + " ms");

        System.out.println(
                "Spring response time: "
                        + springTime + " ms");
    }

    private long measureServletStartup() throws Exception {

        long start = System.nanoTime();

        httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://localhost:"
                                        + SERVLET_PORT
                                        + "/leads"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        return (System.nanoTime() - start) / 1_000_000;
    }

    private long measureSpringBootStartup() throws Exception {

        long start = System.nanoTime();

        httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(
                                "http://localhost:"
                                        + SPRING_PORT
                                        + "/leads"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        return (System.nanoTime() - start) / 1_000_000;
    }

    private int countTableRows(String html) {
        return html.split("<tr").length - 1;
    }
}