package notes;

import helpers.AuthHelper;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class PostNoteTest {

    ResourceBundle credentials = ResourceBundle.getBundle("config");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = credentials.getString("base.url");

    @Test
    public void validPatchNoteReturns200() {
        String token = AuthHelper.generateToken();

        String title =

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"name\":\"%s\", \"phone\":\"%s\", \"company\":\"%s\"}", title, description, category))

    }
}
