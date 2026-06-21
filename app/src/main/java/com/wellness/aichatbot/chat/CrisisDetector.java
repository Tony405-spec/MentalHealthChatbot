package com.wellness.aichatbot.chat;

import java.util.Arrays;
import java.util.List;

public class CrisisDetector {
    private static final List<String> CRISIS_SIGNALS = Arrays.asList(
            "suicide", "kill myself", "end my life", "want to die", "self harm",
            "hurt myself", "can't go on", "no reason to live", "overdose"
    );

    public boolean isCrisis(String normalizedMessage) {
        for (String signal : CRISIS_SIGNALS) {
            if (normalizedMessage.contains(signal)) {
                return true;
            }
        }
        return false;
    }
}
