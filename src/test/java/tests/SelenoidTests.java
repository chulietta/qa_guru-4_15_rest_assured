package tests;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SelenoidTests {

    @Test
    void successStatusTest() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusWithoutGivenTest() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusWithAuthTest() {
        given().when()
                .get("https://user1:1234@selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusWithBasicAuthTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200);
    }

    @Test
    void successStatusResponseTest() {
        Response response = given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response);             // bad log - io.restassured.internal.RestAssuredResponseImpl@ed41518
        System.out.println(response.toString());  // bad log - io.restassured.internal.RestAssuredResponseImpl@ed41518
        System.out.println(response.asString());  // {"value":{"message":"Selenoid 98f495722e60da4b35c14814bae240fe6ec75abc built at 2020-09-02_11:14:20AM","ready":true}}
    }

    @Test
    void successStatusResponseWithLogTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract().response();
    }

    @Test
    void successStatusReadyTest() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void successStatusReadyWithAssertThatAnotherTest() {
        ExtractableResponse<Response> result = given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud:4444/wd/hub/status")
                .then()
                .log().body()
                .statusCode(200)
                .extract();

        assertThat(result.path("value.ready"), is(true));
    }

}
