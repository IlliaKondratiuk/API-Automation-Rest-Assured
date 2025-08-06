package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.test.TestListener;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Listeners(TestListener.class)
public class PatchNoteTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle noteInfo = ResourceBundle.getBundle("note_info");
    ResourceBundle noteMessages = ResourceBundle.getBundle("messages.notes_messages");

    String baseUrl = common.getString("base.url");

    @Test(groups = {"critical", "smoke"})
    public void patchNoteReturns200() {
        String token = AuthHelper.generateToken();

        String id = noteInfo.getString("note.id");
        String completed = noteInfo.getString("note.completed.true");

        String expectedMessage = noteMessages.getString("note.patch.success");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\", \"completed\":\"%s\"}",
                        id, completed))
        .when()
                .patch(baseUrl + ApiEndpoints.NOTES + "/" + id)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

}
