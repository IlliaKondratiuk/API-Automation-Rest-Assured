package helpers;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class AuthHelper {

    private static String token;
    private static final ResourceBundle config = ResourceBundle.getBundle("config");
    private static final String baseUrl = config.getString("base.url");

    public static String generateToken() {
        if (token == null) {
            token = loginAndGetToken();
        }
        return token;
    }

    private static String loginAndGetToken() {
        String email = config.getString("user.email");
        String password = config.getString("user.password");

        Response response = given()
                .contentType("application/json")
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password))
                .when()
                .post(baseUrl + ApiEndpoints.LOGIN);

        response.then().statusCode(HttpStatus.SC_OK);
        return response.jsonPath().getString("data.token");
    }
}
