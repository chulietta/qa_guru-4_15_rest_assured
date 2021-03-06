package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static utils.FileUtils.readStringFromFile;

public class ReqresInTests {

    @BeforeAll
    static void setup() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void successfulCreateUserTest() {
        String data = readStringFromFile("./src/test/resources/create_user_data.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().body()
                .body("name", is("morpheus"));
    }

    @Test
    void successfulRegisterUserTest() {
        String data = readStringFromFile("./src/test/resources/register_user_data.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .log().body()
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unSuccessfulRegisterUserTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"sydney@fife\" }")
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .log().body()
                .body("error", is("Missing password"));
    }

    @Test
    void successfulUpdateUserTest() {
        String data = readStringFromFile("./src/test/resources/update_user_data.txt");
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .put("api/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("job", is("zion resident"));
    }

    @Test
    void successfulDeleteUserTest() {
        given()
                .when()
                .delete("api/users/2")
                .then()
                .statusCode(204);
    }
}
