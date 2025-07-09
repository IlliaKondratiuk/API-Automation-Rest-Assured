package users;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    ResourceBundle credentials = ResourceBundle.getBundle("config");
    ResourceBundle messages = ResourceBundle.getBundle("messages.user_messages");

    String baseUrl = credentials.getString("base.url");

    @Test
    public void validLoginReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String email = credentials.getString("user.email");
        String password = credentials.getString("user.password");
        String validMessage = messages.getString("login.success");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password))
        .when()
                .post(baseUrl + ApiEndpoints.LOGIN)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(validMessage));


    }
}
