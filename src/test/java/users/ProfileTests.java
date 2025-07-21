package users;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProfileTests {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle userInfo = ResourceBundle.getBundle("user_info");
    ResourceBundle userMessages = ResourceBundle.getBundle("messages.user_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test
    public void getProfileReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String expectedMessage = userMessages.getString("profile.successful");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
        .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void patchProfileReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String name = userInfo.getString("user.name");
        String phone = userInfo.getString("user.phone");
        String company = userInfo.getString("user.company");

        String expectedMessage = userMessages.getString("profile.patch");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"name\":\"%s\", \"phone\":\"%s\", \"company\":\"%s\"}", name, phone, company))
        .when()
                .patch(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void getProfileInvalidReturns400() {
        String token = AuthHelper.generateToken();

        String expectedMessage = commonMessages.getString("badrequest.emptybody");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(" ") //making the request invalid
        .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void getProfileUnauthorizedReturns401() {
        String token = AuthHelper.generateToken();

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token + "0") //making the token invalid
        .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void patchProfileMissingNameReturns400() {
        String token = AuthHelper.generateToken();

        String phone = userInfo.getString("user.phone");
        String company = userInfo.getString("user.company");

        String expectedMessage = userMessages.getString("profile.patch.badrequest");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"phone\":\"%s\", \"company\":\"%s\"}", phone, company))
        .when()
                .patch(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void patchProfileUnauthorizedReturns401() {
        String token = AuthHelper.generateToken();

        String name = userInfo.getString("user.name");
        String phone = userInfo.getString("user.phone");
        String company = userInfo.getString("user.company");

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token + "0")
                .body(String.format("{\"name\":\"%s\", \"phone\":\"%s\", \"company\":\"%s\"}", name, phone, company))
        .when()
                .patch(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }
}
