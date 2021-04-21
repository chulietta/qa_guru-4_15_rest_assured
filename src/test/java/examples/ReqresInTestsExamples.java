package examples;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static utils.FileUtils.readStringFromFile;


public class ReqresInTestsExamples {

    @BeforeAll
    static void setup() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void successUsersListTest() {
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .body("support.text",
                        is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void successfulLoginTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }

    @Test
    void unSuccessfulLoginTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"peter@klaven\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    void successfulLoginWithDataInFileTest() {
        String data = readStringFromFile("./src/test/resources/login_data.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is(notNullValue()));
    }
}
