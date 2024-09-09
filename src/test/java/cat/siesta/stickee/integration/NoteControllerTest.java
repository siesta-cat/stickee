package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.persistence.Note;
import cat.siesta.stickee.service.NoteService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import jakarta.annotation.PostConstruct;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "notes.max-size=1KB" })
public class NoteControllerTest {

    private Note noteHello = Note.builder().text("Hello world!").build();
    private Note noteBye = Note.builder().text("Bye!").build();
    private Note noteHtml = Note.builder().text("<b>Bold</b>").build();
    private Note noteJson = Note.builder().text("{ \"text\": \"Hello\" }").build();

    @Autowired
    StickeeConfig stickeeConfig;

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

        given().get(stickeeConfig.getBasePath() + "/" + noteHelloId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello world!"))
                .contentType("text/plain");

        given().get(stickeeConfig.getBasePath() + "/" + noteByeId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Bye!"))
                .contentType("text/plain");
    }

    @Test
    void shouldAlwaysReturnPlainText() {
        String noteHtmlId = noteService.create(noteHtml).getId().orElseThrow();
        String noteJsonId = noteService.create(noteJson).getId().orElseThrow();

        given().header("Accept", "text/html")
                .given().get(stickeeConfig.getBasePath() + "/" + noteHtmlId).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");

        given().header("Accept", "application/json")
                .given().get(stickeeConfig.getBasePath() + "/" + noteJsonId).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain");
    }

    @Test
    void shouldGetNotFoundWhenNotExisting() {
        given().get(stickeeConfig.getBasePath() + "/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    // TODO: add a test for unicode text
    @Test
    void shouldCreateAndGet() {
        var text = "Posting!";

        var response = given()
                .param("text", text)
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        var location = response.header("Location");

        given().get(location).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(text));
    }

    @Test
    void shouldContainCacheHeadersOnGet() {
        var marginSeconds = 5;

        var noteId = noteService.create(noteHello).getId().orElseThrow();
        var expectedCache = stickeeConfig.getMaxAge().toSeconds();
        Stream<String> validRange = IntStream.range(-marginSeconds, marginSeconds)
                .mapToObj(margin -> "max-age=" + (expectedCache + margin) + ", public, immutable");

        var cacheControlValue = given().get(stickeeConfig.getBasePath() + "/" + noteId)
                .header("Cache-control");

        assertTrue(validRange.anyMatch(valid -> valid.equals(cacheControlValue)));
    }

    @Test
    void shouldGetPayloadTooLargeOnBigNote() {
        var text = RandomStringUtils.insecure().nextAscii((int) stickeeConfig.getMaxSize().toBytes() + 1);

        given().param("text", text)
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value());
    }

    @Test
    void shouldGetBadRequestOnEmptyNote() {
        given().param("text", "")
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(containsString("text cannot be empty"));
    }
}
