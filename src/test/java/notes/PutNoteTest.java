package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PutNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle noteInfo = ResourceBundle.getBundle("note_info");
    ResourceBundle noteMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test
    public void putNoteReturns200() {
        String token = AuthHelper.generateToken();

        String id = noteInfo.getString("note.id");
        String title = noteInfo.getString("note.title");
        String description = noteInfo.getString("note.description");
        String completed = noteInfo.getString("note.completed.true");
        String category = noteInfo.getString("note.category.work");

        String expectedMessage = noteMessages.getString("note.put.success");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\", \"title\":\"%s\", \"description\":\"%s\", \"completed\":\"%s\", \"category\":\"%s\"}",
                        id, title, description, completed, category))
        .when()
                .patch(baseUrl + ApiEndpoints.NOTES + "/" + id)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void invalidPutNoteReturns400() {
        String token = AuthHelper.generateToken();

        String id = noteInfo.getString("note.id");
        String expectedMessage = commonMessages.getString("badrequest.emptybody");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(" ")
        .when()
                .patch(baseUrl + ApiEndpoints.NOTES + "/" + id)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test
    public void unauthorizedPutNoteReturns401() { // incorrect token
        String token = AuthHelper.generateToken() + "1"; // deliberately invalid

        String id = noteInfo.getString("note.id");
        String title = noteInfo.getString("note.title");
        String description = noteInfo.getString("note.description");
        String completed = noteInfo.getString("note.completed.true");
        String category = noteInfo.getString("note.category.work");

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\", \"title\":\"%s\", \"description\":\"%s\", \"completed\":\"%s\", \"category\":\"%s\"}",
                        id, title, description, completed, category))
                .when()
                .patch(baseUrl + ApiEndpoints.NOTES + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }


}
