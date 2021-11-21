package de.syrocon.cajee;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

@QuarkusTest
public class VillainsResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body("$", hasKey("name"))
             .body("$", hasKey("otherName"))
             .body("$", hasKey("level"))
             .body("$", hasKey("picture"))
             .body("$", hasKey("powers"));
    }

}