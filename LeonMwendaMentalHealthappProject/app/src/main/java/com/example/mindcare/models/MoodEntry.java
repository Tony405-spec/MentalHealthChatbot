package com.example.mindcare.models;

public class MoodEntry extends BaseEntry {
    private String mood;
    private String notes;

    public MoodEntry() { }

    public MoodEntry(String userId, String mood, String notes) {
        super(userId);
        this.mood = mood;
        this.notes = notes;
    }

    @Override public String displayTitle() { return mood == null ? "Mood" : mood; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
