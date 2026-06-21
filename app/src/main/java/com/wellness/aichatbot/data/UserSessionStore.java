package com.wellness.aichatbot.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionStore {
    private static final String PREF_NAME = "mindful_user_session";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences preferences;

    public UserSessionStore(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void login(String name, String email) {
        preferences.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public void logout() {
        preferences.edit().clear().apply();
    }

    public String getName() {
        return preferences.getString(KEY_NAME, "friend");
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }
}
