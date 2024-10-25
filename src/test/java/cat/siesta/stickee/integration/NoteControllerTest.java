package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.parsing.Parser;
import jakarta.annotation.PostConstruct;

@ActiveProfiles("test")
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "notes.max-size=1KB" })
public class NoteControllerTest {

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
        RestAssured.config = RestAssured.config().encoderConfig(
                EncoderConfig.encoderConfig().defaultContentCharset(Charset.defaultCharset()));
    }

    @Test
    void shouldGetWhenExisting() {
        var note = NoteStub.builder().build();
        var noteId = noteService.create(note).getMaybeId().orElseThrow();

        given().get(stickeeConfig.getBasePath() + "/" + noteId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(note.getText()))
                .contentType("text/plain");
    }

    @Test
    void shouldGetFromRawPath() {
        var note = NoteStub.builder().build();
        var noteId = noteService.create(note).getMaybeId().orElseThrow();

        given().get(stickeeConfig.getBasePath() + "/raw/" + noteId).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(note.getText()))
                .contentType("text/plain");
    }

    @ParameterizedTest
    @CsvSource({
            "text/html,<b>Bold</b>",
            "application/json,{ \"text\": \"Hello\" }"
    })
    void contentTypeIsAlwaysPlainTextRegardlessOfAcceptHeader(String acceptedContentType, String text) {
        var note = NoteStub.builder().text(text).build();
        var noteId = noteService.create(note).getMaybeId().orElseThrow();

        given().header("Accept", acceptedContentType)
                .given().get(stickeeConfig.getBasePath() + "/" + noteId).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain")
                .body(equalTo(text));
    }

    @Test
    void shouldGetNotFoundWhenNotExisting() {
        given().get(stickeeConfig.getBasePath() + "/" + UUID.randomUUID()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldCreateAndGetUnicode() {
        var text = RandomStringUtils.insecure().next(10);

        var response = given()
                .param("text", text)
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        var location = response.header("Location");

        given().get(location).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("html.body.section.textarea", Matchers.containsString(text));
    }

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
                .body("html.body.section.textarea", Matchers.containsString(text));
    }

    @Test
    void locationHeaderAndBodySouldReturnSame() {
        var text = "Posting!";

        var response = given()
                .param("text", text)
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        var location = response.header("Location");
        var body = response.body().asString();

        given().get(location).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("html.body.section.textarea", Matchers.containsString(text));

        given().get("http://" + body).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo(text));

        assertTrue(body.contains("localhost:" + RestAssured.port));
    }

    @Test
    void shouldContainCacheHeadersOnGet() {
        var marginSeconds = 60;

        var note = NoteStub.builder().build();
        var noteId = noteService.create(note).getMaybeId().orElseThrow();
        var expectedCache = stickeeConfig.getMaxExpirationTime().toSeconds();
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

    @Test
    void shouldCreateNoteWithCustomTimestamp() {
        var text = RandomStringUtils.insecure().next(10);

        var expirationTime = Duration.ofHours(1);

        var response = given()
                .param("text", text)
                .param("expirationTime", expirationTime.toString())
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        var location = response.header("Location");
        var id = location.substring(location.lastIndexOf("/") + 1);

        var note = noteService.get(id).get();

        assertTrue(Duration
                .between(note.getCreationTimestamp().plus(expirationTime),
                        note.getExpirationTimestamp())
                .abs().compareTo(Duration.ofMinutes(1)) == -1);
    }

    @Test
    void shouldReturnBadRequestWithTooBigExpiration() {
        var text = RandomStringUtils.insecure().next(10);

        given().param("text", text)
                .param("expirationTime", stickeeConfig.getMaxExpirationTime().plusDays(1).toString())
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnBadRequestWithInvalidExpiration() {
        var text = RandomStringUtils.insecure().next(10);
        var invalidExpiration = "invalid";

        given().param("text", text)
                .param("expirationTime", invalidExpiration)
                .post(stickeeConfig.getBasePath() + "/create").then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
