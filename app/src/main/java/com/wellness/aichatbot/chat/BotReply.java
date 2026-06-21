package com.wellness.aichatbot.chat;

public class BotReply {
    private final IntentType intentType;
    private final String text;
    private final boolean crisis;

    public BotReply(IntentType intentType, String text, boolean crisis) {
        this.intentType = intentType;
        this.text = text;
        this.crisis = crisis;
    }

    public IntentType getIntentType() {
        return intentType;
    }

    public String getText() {
        return text;
    }

    public boolean isCrisis() {
        return crisis;
    }
}
