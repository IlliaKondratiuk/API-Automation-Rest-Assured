package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.test.TestListener;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Notes API")
@Feature("POST /notes endpoint")
@Owner("Illia")
@Listeners(TestListener.class)
public class PostNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test(groups = {"critical", "smoke"})
    public void validPostNoteReturns200() {
        String token = AuthHelper.generateToken();

        String title = info.getString("title");
        String description = info.getString("description");
        String category = info.getString("valid_category");

        String expectedMessage = notesMessages.getString("note.patch.created");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"category\":\"%s\"}", title, description, category))
        .when()
                .post(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test(groups = {"critical", "smoke"})
    public void invalidPostNoteReturns400() { //missing category
        String token = AuthHelper.generateToken();

        String title = info.getString("title");
        String description = info.getString("description");

        String expectedMessage = notesMessages.getString("note.patch.badrequest");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"title\":\"%s\", \"description\":\"%s\"}", title, description))
        .when()
                .post(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test(groups = {"critical", "smoke"})
    public void unauthorizedPostNoteReturns401() { //incorrect token
        String token = AuthHelper.generateToken() + "1";

        String title = info.getString("title");
        String description = info.getString("description");
        String category = info.getString("valid_category");

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"category\":\"%s\"}", title, description, category))
        .when()
                .post(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }
}