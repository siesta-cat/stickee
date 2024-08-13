package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NoteControllerIntegrationTest {

    private Note noteHello = new Note("Hello world!");
    private Note noteBye = new Note("Bye!");
    private Note noteHtml = new Note("<b>Bold</b>");
    private Note noteJson = new Note("{ \"text\": \"Hello\" }");

    @Value("${stickee.notes-base-path}")
    private String notesBasePath;

    @Autowired
    NoteService noteService;

    @LocalServerPort
    Integer port;

    @PostConstruct
    void setUp() {
        RestAssured.port = port;
        RestAssured.registerParser("text/plain", Parser.TEXT);
    }

    @Test
    void shouldGetWhenExisting() {
        String noteHelloId = noteService.create(noteHello).getId().orElseThrow();
        String noteByeId = noteService.create(noteBye).getId().orElseThrow();

        given().get(notesBasePath + "/" + noteHelloId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello world!"))
                .contentType("text/plain");

        given().get(notesBasePath + "/" + noteByeId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Bye!"))
                .contentType("text/plain");
    }

    @Test
    void shouldAlwaysReturnPlainText() {
        String noteHtmlId = noteService.create(noteHtml).getId().orElseThrow();
        String noteJsonId = noteService.create(noteJson).getId().orElseThrow();

        given().header("Accept", "text/html")
            .given().get(notesBasePath + "/" + noteHtmlId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");
        
        given().header("Accept", "application/json")
            .given().get(notesBasePath + "/" + noteJsonId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");
    }

    @Test
    void shouldGetNotFoundWhenNotExisting() {
        given().get(notesBasePath + "/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldCreateAndGet() {
        var text = "Posting!";

        var response = given()
                .param("text", text)
                .post(notesBasePath + "/create").then().assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        var location = response.header("Location");

        given().get(location).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(text));
    }
}
