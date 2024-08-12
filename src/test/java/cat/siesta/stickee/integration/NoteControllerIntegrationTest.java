package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class NoteControllerIntegrationTest {

    private Note noteHello = new Note("Hello world!");
    private Note noteBye = new Note("Bye!");
    private Note noteHtml = new Note("<b>Bold</b>");
    private Note noteJson = new Note("{ \"text\": \"Hello\"}");

    @Value("${stickee.notes-base-path}")
    private String notesBasePath;

    @Autowired
    NoteService noteService;

    @BeforeAll
    static void registerParser() {
        RestAssured.registerParser("text/plain", Parser.TEXT);
    }

    @Test
    void shouldGetWhenExisting() {
        String noteHelloId = noteService.create(noteHello).getResourceLocator().orElseThrow();
        String noteByeId = noteService.create(noteBye).getResourceLocator().orElseThrow();

        when().get(notesBasePath + "/" + noteHelloId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello world!"))
                .contentType("text/plain");

        when().get(notesBasePath + "/" + noteByeId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Bye!"))
                .contentType("text/plain");
    }

    @Test
    void shouldAlwaysReturnPlainText() {
        String noteHtmlId = noteService.create(noteHtml).getResourceLocator().orElseThrow();
        String noteJsonId = noteService.create(noteJson).getResourceLocator().orElseThrow();

        given().header("Accept", "text/html")
            .when().get(notesBasePath + "/" + noteHtmlId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");
        
        given().header("Accept", "application/json")
            .when().get(notesBasePath + "/" + noteJsonId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");
    }

    @Test
    void shouldGetNotFoundWhenNotExisting() {
        when().get(notesBasePath + "/" + UUID.randomUUID()).then().assertThat()
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

        when().get(location).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(text));
    }
}
