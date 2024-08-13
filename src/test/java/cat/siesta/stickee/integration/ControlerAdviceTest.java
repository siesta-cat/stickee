package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import cat.siesta.stickee.service.NoteService;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControlerAdviceTest {

    @Value("${stickee.notes-base-path}")
    private String notesBasePath;

    @MockBean
    NoteService noteService;

    @LocalServerPort
    Integer port;

    @PostConstruct
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testUnhandledException() throws Exception {
        given(noteService.get(any())).willThrow(RuntimeException.class);

        given().get(notesBasePath + "/" + "awjendkewjn").then().assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
