package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");

    String baseUrl = common.getString("base.url");

    @Test
    public void validPatchNoteReturns200() {
        String token = AuthHelper.generateToken();

        String title = info.getString("title");
        String description = info.getString("description");
        String category = info.getString("valid_category");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"category\":\"%s\"}", title, description, category))
        .when()
                .post(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(notesMessages.getString("note.patch.created")));
    }
}
