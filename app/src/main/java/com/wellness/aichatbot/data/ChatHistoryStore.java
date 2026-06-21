package com.wellness.aichatbot.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryStore {
    private static final String PREF_NAME = "mindful_chat_history";
    private static final String KEY_MESSAGES = "messages";

    private final SharedPreferences preferences;
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<ChatMessage>>() {}.getType();

    public ChatHistoryStore(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public List<ChatMessage> load() {
        String json = preferences.getString(KEY_MESSAGES, "[]");
        List<ChatMessage> messages = gson.fromJson(json, listType);
        return messages == null ? new ArrayList<>() : messages;
    }

    public void save(List<ChatMessage> messages) {
        preferences.edit().putString(KEY_MESSAGES, gson.toJson(messages, listType)).apply();
    }

    public void append(ChatMessage message) {
        List<ChatMessage> messages = load();
        messages.add(message);
        save(messages);
    }
}
