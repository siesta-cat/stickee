package cat.siesta.stickee.integration;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(statements = {
    "INSERT INTO note (resource_locator, text) VALUES ('f92', 'Hello world!')",
    "INSERT INTO note (resource_locator, text) VALUES ('3c2', 'Bye!')"
}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class NoteControllerIntegrationTest {

    @BeforeAll
    static void registerParser() {
        RestAssured.registerParser("text/plain", Parser.TEXT);
    }

    @Test
    void shouldGetWhenExisting() {
        when().get("/f92").then().assertThat()
            .statusCode(200)
            .body(equalTo("Hello world!"));

        when().get("/3c2").then().assertThat()
            .statusCode(200)
            .body(equalTo("Bye!"));
    }

    @Test
    void shouldGetNotFoundWhenNotExisting() {
        when().get("/000").then().assertThat()
            .statusCode(404);
    }
}
