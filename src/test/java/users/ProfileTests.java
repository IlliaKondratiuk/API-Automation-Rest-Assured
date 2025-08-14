package users;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.test.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Notes API")
@Feature("GET and PATCH /profile endpoint")
@Owner("Illia")
@Listeners(TestListener.class)
public class ProfileTests {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle userInfo = ResourceBundle.getBundle("user_info");
    ResourceBundle userMessages = ResourceBundle.getBundle("messages.user_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test(groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void getProfileReturns200() {
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

    @Test(groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void patchProfileReturns200() {
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

    @Test(groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
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

    @Test(groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void getProfileUnauthorizedReturns401() {
        String token = AuthHelper.generateToken() + 1; //incorrect token

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
        .when()
                .get(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }

    @Test(groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
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

    @Test(groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void patchProfileUnauthorizedReturns401() {
        String token = AuthHelper.generateToken() + 1; //incorrect token

        String name = userInfo.getString("user.name");
        String phone = userInfo.getString("user.phone");
        String company = userInfo.getString("user.company");

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"name\":\"%s\", \"phone\":\"%s\", \"company\":\"%s\"}", name, phone, company))
        .when()
                .patch(baseUrl + ApiEndpoints.PROFILE)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }
}
