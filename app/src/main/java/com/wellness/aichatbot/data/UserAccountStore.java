package com.wellness.aichatbot.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserAccountStore {
    private static final String PREF_NAME = "mindful_user_accounts";
    private final SharedPreferences preferences;

    public UserAccountStore(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean register(String name, String email, String password) {
        if (preferences.contains(email)) {
            return false; // Already exists
        }
        preferences.edit()
                .putString(email, password + "|" + name)
                .apply();
        return true;
    }

    public String authenticate(String email, String password) {
        String data = preferences.getString(email, null);
        if (data == null) return null;
        
        String[] parts = data.split("\\|");
        if (parts.length == 2 && parts[0].equals(password)) {
            return parts[1]; // Return Name
        }
        return null;
    }
}
