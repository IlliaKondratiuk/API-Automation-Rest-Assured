package models;

public class Note {

    private String id;
    private String title;
    private String description;
    private String category;
    private boolean completed;
    private String created_at;
    private String updated_at;
    private String user_id;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getUser_id() {
        return user_id;
    }
}
