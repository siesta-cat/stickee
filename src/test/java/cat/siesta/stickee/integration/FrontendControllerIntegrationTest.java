package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FrontendControllerIntegrationTest {

    @LocalServerPort
    Integer port;

    @PostConstruct
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testHome() throws Exception {
        when().get("/").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    public void testCss() throws Exception {
        when().get("/main.css").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/css");
    }
}
