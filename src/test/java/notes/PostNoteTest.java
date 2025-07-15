package notes;

import java.util.ResourceBundle;

public class PostNoteTest {

    ResourceBundle credentials = ResourceBundle.getBundle("config");
    ResourceBundle notesMessages = ResourceBundle.getBundle("messages.notes_messages");
    ResourceBundle commonMessages = ResourceBundle.getBundle("messages.common_messages");

    String baseUrl = credentials.getString("base.url");


}
