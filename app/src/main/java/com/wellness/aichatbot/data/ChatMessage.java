package com.wellness.aichatbot.data;

public class ChatMessage {
    public static final String SENDER_USER = "user";
    public static final String SENDER_BOT = "bot";

    private String sender;
    private String text;
    private long timestamp;

    // Required for Firestore
    public ChatMessage() {}

    public ChatMessage(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUser() {
        return SENDER_USER.equals(sender);
    }
}
