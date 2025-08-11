package notes;

import helpers.ApiEndpoints;
import helpers.AuthHelper;
import helpers.test.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import models.GetNotesResponse;
import models.Note;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Notes API")
@Feature("GET /notes endpoint")
@Owner("Illia")
@Listeners(TestListener.class)
public class GetNotesTest {

    ResourceBundle common = ResourceBundle.getBundle("common.common_info");
    ResourceBundle info = ResourceBundle.getBundle("note_info");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = common.getString("base.url");

    @Test(description = "Valid get all existing notes", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void validGetNotesReturns200() {
        String token = AuthHelper.generateToken();

        String expectedMessage = notesMessages.getString("note.get.success");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
        .when()
                .get(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test(description = "Checking all fields of a valid note are correct", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
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

    @Test(description = "Valid get an existing note by ID", groups = {"critical", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void getNoteByIdReturns200() {
        String token = AuthHelper.generateToken();

        String expectedMessage = notesMessages.getString("note.get.success");
        int noteId = 1;

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(String.format("{\"id\":\"%s\"}", noteId))
        .when()
                .get(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(expectedMessage));
    }

    @Test(description = "Invalid get existing note with empty body", groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void invalidGetNoteByIdReturns400() {
        String token = AuthHelper.generateToken();

        String expectedMessage = commonMessages.getString("badrequest.emptybody");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
                .body(" ")
        .when()
                .get(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(expectedMessage));
    }

    @Test(description = "Invalid get notes with an invalid token", groups = {"normal", "smoke"})
    @Severity(SeverityLevel.NORMAL)
    public void getNotesUnauthorizedReturns401() {
        String token = AuthHelper.generateToken() + 1;

        String expectedMessage = commonMessages.getString("unauthorized");

        given()
                .contentType(ContentType.JSON)
                .header("x-auth-token", token)
        .when()
                .get(baseUrl + ApiEndpoints.NOTES)
        .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(expectedMessage));
    }
}