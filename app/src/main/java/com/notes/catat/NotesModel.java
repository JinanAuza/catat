package com.notes.catat;

public class NotesModel {
    private int id;
    private String title;
    private String content;

    // Konstruktor untuk NotesModel
    public NotesModel(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter dan Setter untuk id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter untuk title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter dan Setter untuk content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
