package com.example.mindcare.models;

public class JournalEntry extends BaseEntry {
    private String type;
    private String content;

    public JournalEntry() { }

    public JournalEntry(String userId, String type, String content) {
        super(userId);
        this.type = type;
        this.content = content;
    }

    @Override public String displayTitle() { return type == null ? "Journal" : type; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
