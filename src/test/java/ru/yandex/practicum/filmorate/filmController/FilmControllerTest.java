package ru.yandex.practicum.filmorate.filmController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void postFilmValidTest() throws IOException, InterruptedException {
        String json = """
                {
                  "id": 1,
                  "name": "Крепкий орешек",
                  "description": "Что то невероятное",
                  "releaseDate": "2000-07-15",
                  "duration": "PT1H51M"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getFilmsValidTest() throws IOException, InterruptedException {
        String json = """
                {
                  "id": 1,
                  "name": "Крепкий орешек",
                  "description": "Что то невероятное",
                  "releaseDate": "2000-07-15",
                  "duration": "PT1H51M"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
//		Запрос на добавление фильма.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		GET запрос.
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
    }

    @Test
    void postFilmUnValidDurationTest() throws IOException, InterruptedException {
//		"-" в длительности
        String json = """
                {
                  "id": 1,
                  "name": "Крепкий орешек",
                  "description": "Что то невероятное",
                  "releaseDate": "2000-07-15",
                  "duration": "-PT1H51M"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void postFilmUnValidNameTest() throws IOException, InterruptedException {
//		Пустое имя
        String json = """
                {
                  "id": 1,
                  "name": " ",
                  "description": "Что то невероятное",
                  "releaseDate": "2000-07-15",
                  "duration": "PT1H51M"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void postFilmUnValidDescriptionTest() throws IOException, InterruptedException {
//		Длинна описания более 200 символов
        String json = """
                {
                   "id": 1,
                   "name": "Крепкий орешек",
                   "description": "Фильм очень, очень, очень, очень, очень, очень, очень, очень, очень, очень, очень, очень, очень, оченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьоченьочень интересный)",
                   "releaseDate": "2000-07-15",
                   "duration": "PT1H51M"
                 }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void postFilmUnValidReleaseDateTest() throws IOException, InterruptedException {
        // Дата: "1895-12-27"
        String json = """
                {
                  "id": 1,
                  "name": "Крепкий орешек",
                  "description": "Что то невероятное",
                  "releaseDate": "1895-12-27",
                  "duration": "PT1H51M"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/films"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
