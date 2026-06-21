package com.example.mindcare.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static final String FILE = "mindcare_prefs";
    private final SharedPreferences prefs;

    public Prefs(Context context) {
        prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public void setRememberMe(boolean value) { prefs.edit().putBoolean("remember", value).apply(); }
    public boolean isRememberMe() { return prefs.getBoolean("remember", false); }
    public void setGreyTheme(boolean value) { prefs.edit().putBoolean("grey_theme", value).apply(); }
    public boolean isGreyTheme() { return prefs.getBoolean("grey_theme", false); }
    public void setLastIntent(String intent) { prefs.edit().putString("last_intent", intent).apply(); }
    public String getLastIntent() { return prefs.getString("last_intent", "General Wellness"); }
    public void clearSession() { prefs.edit().putBoolean("remember", false).apply(); }
}
