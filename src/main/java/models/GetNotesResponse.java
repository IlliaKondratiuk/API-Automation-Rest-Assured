package models;

import java.util.List;

public class GetNotesResponse {

    private boolean success;
    private int status;
    private String message;
    private List<Note> data;

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Note> getData() {
        return data;
    }
}
