package users;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.sql.SQLOutput;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProfileTests {

    ResourceBundle credentials = ResourceBundle.getBundle("config");
    ResourceBundle userMessages = ResourceBundle.getBundle("messages.user_messages");

    String baseUrl = credentials.getString("base.url");

    @Test
    public void getProfileReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String SuccessMessage = userMessages.getString("profile.successful");

       given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(SuccessMessage));
    }

    @Test
    public void patchProfileReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String name = credentials.getString("user.name");
        String phone = credentials.getString("user.phone");
        String company = credentials.getString("user.company");

        String patchSuccessMessage = userMessages.getString("profile.patch");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"name\":\"%s\", \"phone\":\"%s\", \"company\":\"%s\"}", name, phone, company))
                .when()
                .patch(baseUrl + ApiEndpoints.PROFILE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(patchSuccessMessage));
    }

    @Test
    public void getProfileInvalidReturns400() {
        String token = AuthHelper.generateToken();

        String invalidMessage = userMessages.getString("profile.get.badrequest");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(" ") //making the request invalid
                .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(invalidMessage));
    }

    @Test
    public void getProfileUnauthorizedReturns401() {
        String token = AuthHelper.generateToken();

        String unauthorizedMessage = userMessages.getString("profile.get.unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token + "0") //making the token invalid
                .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(unauthorizedMessage));
    }
}
