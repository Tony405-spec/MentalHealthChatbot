package com.example.mindcare.models;

import java.util.Arrays;
import java.util.List;

public class WellnessIntent {
    private final String name;
    private final List<String> actions;

    public WellnessIntent(String name, String... actions) {
        this.name = name;
        this.actions = Arrays.asList(actions);
    }

    public String getName() { return name; }
    public List<String> getActions() { return actions; }
}
