package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import io.restassured.http.ContentType;
import models.GetNotesResponse;
import models.Note;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetNotesTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test
    public void getNotesReturns200() {
        String token = AuthHelper.generateToken();

        String successMessage = notesMessages.getString("note.get.success");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
        .when()
                .get(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(successMessage));
    }

    @Test
    public void getNotesHasCorrectFields() {
        String token = AuthHelper.generateToken();

        GetNotesResponse response = given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .when()
                .get(baseUrl + ApiEndpoints.NOTES)
                .then()
                .extract()
                .as(GetNotesResponse.class);

        List<Note> notes = response.getData();

        for(Note note : notes) {
            Assert.assertNotNull(note.getId());
            Assert.assertNotNull(note.getTitle());
            Assert.assertNotNull(note.getDescription());
            Assert.assertNotNull(note.getCategory());
            Assert.assertNotNull(note.getCreated_at());
            Assert.assertNotNull(note.getUpdated_at());
            Assert.assertEquals(note.getUser_id(), info.getString("expected_user_id"));
        }
    }
}
