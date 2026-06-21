package com.wellness.aichatbot.chat;

import java.util.Arrays;
import java.util.List;

public class IntentPattern {
    private final IntentType type;
    private final List<String> signals;
    private final String response;

    public IntentPattern(IntentType type, String response, String... signals) {
        this.type = type;
        this.response = response;
        this.signals = Arrays.asList(signals);
    }

    public IntentType getType() {
        return type;
    }

    public String getResponse() {
        return response;
    }

    public int score(String normalizedMessage) {
        int score = 0;
        for (String signal : signals) {
            if (normalizedMessage.contains(signal)) {
                score += signal.contains(" ") ? 3 : 1;
            }
        }
        return score;
    }
}
