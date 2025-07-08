package auth;

import io.restassured.response.Response;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class AuthHelper {

    private static String token;
    private static final ResourceBundle config = ResourceBundle.getBundle("config");

    public static String generateToken() {
        if (token == null) {
            token = loginAndGetToken();
        }
        return token;
    }

    private static String loginAndGetToken() {

        String baseUrl = config.getString("baseUrl");
        String email = config.getString("email");
        String password = config.getString("password");

        Response response = given()
                .contentType("application/json")
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password))
                .when()
                .post(baseUrl + "/api/login");

        response.then().statusCode(200);

        return response.jsonPath().getString("token");
    }
}
