package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.test.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Notes API")
@Feature("DELETE /notes endpoint")
@Owner("Illia")
@Listeners(TestListener.class)
public class DeleteNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    String idToDelete;

    @BeforeTest
    public void createNoteToDelete() {
        String token = AuthHelper.generateToken();

        String title = info.getString("title");
        String description = info.getString("description");
        String category = info.getString("valid_category");

        Response response = given()
             .contentType(ContentType.JSON)
             .header("x-auth-token", token)
             .body(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"category\":\"%s\"}", title, description, category))
        .when()
             .post(baseUrl + ApiEndpoints.NOTES);

        idToDelete = response.path("data.id");
    }

    @Test(description = "Delete a valid existing note", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void deleteValidNoteReturns200() {
        String token = AuthHelper.generateToken();

        String expectedMessage = notesMessages.getString("note.delete.success");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\"}", idToDelete))
        .when()
                .delete(baseUrl + ApiEndpoints.NOTES + "/" + idToDelete)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test(description = "Invalid delete request with empty json body", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void invalidDeleteNoteReturns400() {
        String token = AuthHelper.generateToken();

        String expectedMessage = commonMessages.getString("badrequest.emptybody");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(" ") // empty ID
        .when()
                .delete(baseUrl + ApiEndpoints.NOTES + "/" + idToDelete)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test(description = "Invalid delete request with an invalid token", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void unauthorizedDeleteNoteReturns401() {
        String token = AuthHelper.generateToken() + "1"; // invalid token

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\"}", idToDelete))
                .when()
                .delete(baseUrl + ApiEndpoints.NOTES + "/" + idToDelete)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }
}
