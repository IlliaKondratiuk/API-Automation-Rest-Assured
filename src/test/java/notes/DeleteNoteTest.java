package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    String idToDelete;

    @BeforeTest
    public void validPostNoteReturns200() {
        String token = AuthHelper.generateToken();

        String title = info.getString("title");
        String description = info.getString("description");
        String category = info.getString("valid_category");

        String expectedMessage = notesMessages.getString("note.patch.created");

        Response response = given()
             .contentType(ContentType.JSON)
             .header("x-auth-token", token)
             .body(String.format("{\"title\":\"%s\", \"description\":\"%s\", \"category\":\"%s\"}", title, description, category))
        .when()
             .post(baseUrl + ApiEndpoints.NOTES);

        idToDelete = response.path("data.id");


    }

}
