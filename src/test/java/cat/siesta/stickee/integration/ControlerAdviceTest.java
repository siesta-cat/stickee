package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import cat.siesta.stickee.config.StickeeConfig;
import cat.siesta.stickee.service.NoteService;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControlerAdviceTest {

    @Autowired
    StickeeConfig stickeeConfig;

    @MockBean
    NoteService noteService;

    @LocalServerPort
    Integer port;

    @PostConstruct
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testUnhandledException() {
        given(noteService.get(any())).willThrow(new RuntimeException("Test exception"));

        given().get(stickeeConfig.getNotesBasePath() + "/" + "awjendkewjn").then().assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void testStaticResourceNotFound() {
        given().get("/unexisting.css").then().assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
