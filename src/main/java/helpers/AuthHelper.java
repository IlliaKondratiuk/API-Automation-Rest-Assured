package helpers;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class AuthHelper {

    private static String token;
    private static final ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    private static final ResourceBundle userInfo = ResourceBundle.getBundle("user_info");

    private static final String baseUrl = common.getString("base.url");

    public static String generateToken() {
        if (token == null) {
            token = loginAndGetToken();
        }
        return token;
    }

    private static String loginAndGetToken() {
        String email = userInfo.getString("user.email");
        String password = userInfo.getString("user.password");

        Response response = given()
                .contentType("application/json")
                .body(String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password))
                .when()
                .post(baseUrl + ApiEndpoints.LOGIN);

        response.then().statusCode(HttpStatus.SC_OK);
        return response.jsonPath().getString("data.token");
    }
}
