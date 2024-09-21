package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import cat.siesta.stickee.domain.NoteId;
import cat.siesta.stickee.service.NoteService;
import cat.siesta.stickee.utils.NoteStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FrontendControllerTest {

    @Autowired
    NoteService noteService;

    @LocalServerPort
    Integer port;

    @PostConstruct
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testHome() {
        given().get("/").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    void testCss() {
        given().get("/main.css").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/css");
    }

    @Test
    void testJs() {
        given().get("/color-modes.js").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/javascript");
    }

    @Test
    void shouldGetDetailedView() {
        var id = noteService.create(NoteStub.builder().build()).getMaybeId().get();
        given().get("/notes/detail/" + id).then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    void shouldNotFoundNonExistingNote() {
        given().get("/notes/detail/" + NoteId.generate()).then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.HTML);
    }
}
