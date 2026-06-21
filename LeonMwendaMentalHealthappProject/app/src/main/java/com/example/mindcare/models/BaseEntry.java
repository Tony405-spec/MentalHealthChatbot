package com.example.mindcare.models;

public abstract class BaseEntry {
    private String id;
    private String userId;
    private long createdAt;

    public BaseEntry() { }

    public BaseEntry(String userId) {
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
    }

    public abstract String displayTitle();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
