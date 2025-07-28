package ru.yandex.practicum.filmorate.userController;


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
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void postUserValidTest() throws IOException, InterruptedException {


        String json = """
                {
                  "id": 1,
                  "email": "dimonKrytoi@Yandex.ru",
                  "login": "ДимонКрутой",
                  "name": "Дмитрий",
                  "birthday": "1988-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void postUserWithOutNameValidTest() throws IOException, InterruptedException {


        String json = """
                {
                  "id": 1,
                  "email": "dimonKrytoi@Yandex.ru",
                  "login": "ДимонКрутой",
                  "name": "     ",
                  "birthday": "1988-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getUsersValidTest() throws IOException, InterruptedException {


        String json = """
                {
                  "id": 1,
                  "email": "dimonKrytoi@Yandex.ru",
                  "login": "ДимонКрутой",
                  "name": "Дмитрий",
                  "birthday": "1988-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
//		Запрос на добавление пользователя.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		GET запрос.
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
    }


    @Test
    void postUserUnValidEmailTest() throws IOException, InterruptedException {


//		Кривая почта
        String json = """
                {
                  "id": 1,
                  "email": "ДимонКрутобокий@",
                  "login": "ДимонКрутой",
                  "name": "Дмитрий",
                  "birthday": "1988-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void postUserUnValidLoginTest() throws IOException, InterruptedException {


//		Пробелы в поле login
        String json = """
                {
                  "id": 1,
                  "email": "ДимонКрутобокий@yandex.ru",
                  "login": "  Логину не нравятся пробелы  ",
                  "name": "Дмитрий",
                  "birthday": "1988-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void postUserUnValidDateTest() throws IOException, InterruptedException {


//		Дата рождения в будущем
        String json = """
                {
                  "id": 1,
                  "email": "ДимонКрутобокий@yandex.ru",
                  "login": "Логинунравитсябезпробелов",
                  "name": "Дмитрий",
                  "birthday": "2030-07-15"
                }""";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
