package users;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.InvalidTestData;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    private static final ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    private static final ResourceBundle credentials = ResourceBundle.getBundle("user_info");
    private static final ResourceBundle userMessages = ResourceBundle.getBundle("messages.user_messages");

    String baseUrl = common.getString("base.url");

    @Test
    public void validLoginReturnsSuccess() {
        String token = AuthHelper.generateToken();

        String email = credentials.getString("user.email");
        String password = credentials.getString("user.password");
        String expectedMessage = userMessages.getString("login.success");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password))
        .when()
                .post(baseUrl + ApiEndpoints.LOGIN)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void loginWithoutCredentialsReturns400() {
        String token = AuthHelper.generateToken();

        String expectedMessage = userMessages.getString("login.novalidemail");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\"password\":\"" + InvalidTestData.WRONG_PASSWORD + "\"}")
                .when()
                .post(baseUrl + ApiEndpoints.LOGIN)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void loginWithInvalidCredentialsReturns401() {
        String token = AuthHelper.generateToken();

        String invalidCredentialsMessage = userMessages.getString("login.incorrectcredentials");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}",
                        InvalidTestData.INVALID_EMAIL, InvalidTestData.WRONG_PASSWORD))
                .when()
                .post(baseUrl + ApiEndpoints.LOGIN)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(invalidCredentialsMessage));
    }
}
