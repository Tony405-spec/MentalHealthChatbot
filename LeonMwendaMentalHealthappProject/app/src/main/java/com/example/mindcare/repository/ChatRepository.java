package com.example.mindcare.repository;

import com.example.mindcare.models.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final List<ChatMessage> session = new ArrayList<>();

    public List<ChatMessage> history() {
        return session;
    }

    public void add(ChatMessage message) {
        session.add(message);
    }

    public boolean isCrisis(String text) {
        if (text == null) return false;
        String value = text.toLowerCase(java.util.Locale.ROOT);
        return value.contains("suicide") || value.contains("kill myself") || value.contains("self harm")
                || value.contains("hurt myself") || value.contains("end my life");
    }

    public String responseFor(String intent) {
        switch (intent) {
            case "Anxiety":
                return "Anxiety can feel loud, but you are not broken. Try this: breathe in for 4, hold for 2, breathe out for 6. Then write: What is one thing I know is true right now?";
            case "Stress":
                return "Stress often asks for everything at once. Choose the next smallest step, set a 10 minute timer, then pause. Your quick actions: breathe, prioritize, hydrate, and release one non-urgent task.";
            case "Sleep Problems":
                return "For sleep, make the room cool, dim screens 45 minutes before bed, and keep a repeatable routine: wash, stretch, write one worry down, then breathe slowly.";
            case "Low Motivation":
                return "Low motivation is not a character flaw. Pick one two-minute action and let that be enough to begin. A tiny win can restart momentum.";
            case "Panic":
                return "Panic is frightening, but it passes. Put both feet on the floor, press your toes down, exhale slowly, and name five safe things around you.";
            case "Depression":
                return "I cannot diagnose you, but I can sit with you in this moment. Try one care anchor: water, food, daylight, clean clothes, or reaching out to someone safe.";
            case "Burnout":
                return "Burnout asks for recovery, not more pressure. Reduce input, protect one rest block, and choose a task that can be postponed or shared.";
            case "Overthinking":
                return "When thoughts loop, externalize them. Write the worry, the evidence, and one balanced thought. Then return to one concrete action.";
            case "Loneliness":
                return "Loneliness hurts. Send one low-pressure message: 'Thinking of you. Can we talk this week?' Connection can start softly.";
            case "Academic Pressure":
                return "Break the work into visible pieces: review brief, open document, write messy outline, work for 25 minutes, then stop and reset.";
            case "Self-Esteem":
                return "Speak to yourself like someone you are responsible for caring for. Prompt: What would I say to a friend feeling this exact thing?";
            case "Relationship Issues":
                return "Pause before reacting. Name the feeling, the need, and the request. Try: 'I feel __ because __. Could we __?'";
            default:
                return "I am here with you. Choose one gentle support: breathe, journal, stretch, drink water, or check in with someone you trust.";
        }
    }
}
