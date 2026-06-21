package com.example.mindcare.models;

public class ChatMessage {
    private String text;
    private boolean user;
    private long timestamp;

    public ChatMessage() { }

    public ChatMessage(String text, boolean user) {
        this.text = text;
        this.user = user;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isUser() { return user; }
    public void setUser(boolean user) { this.user = user; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
