package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class FrontendControllerIntegrationTest {

    @Test
    public void testHome() throws Exception {
        when().get("/").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    public void testCSS() throws Exception {
        when().get("/main.css").then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/css");
    }
}
