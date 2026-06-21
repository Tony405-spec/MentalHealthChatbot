package com.example.mindcare.utils;

import android.content.Context;

public class FirebaseConfigUtils {
    private static final String PLACEHOLDER_KEY = "REPLACE_WITH_FIREBASE_WEB_API_KEY";

    public static boolean hasValidFirebaseApiKey(Context context) {
        int id = context.getResources().getIdentifier("google_api_key", "string", context.getPackageName());
        if (id == 0) return false;
        String key = context.getString(id);
        return key != null
                && !key.trim().isEmpty()
                && !PLACEHOLDER_KEY.equals(key)
                && !key.toLowerCase().contains("replace_with")
                && !key.toLowerCase().contains("placeholder");
    }

    public static String setupMessage() {
        return "Firebase setup incomplete. Please ensure 'Email/Password' is enabled in the Firebase Console (Authentication > Sign-in method).";
    }

    public static String friendlyAuthError(Exception error) {
        if (error == null || error.getMessage() == null) return "Authentication failed. Please try again.";
        String message = error.getMessage();
        String lower = message.toLowerCase();
        
        if (lower.contains("api key not valid") || lower.contains("api_key_invalid")) {
            return "Invalid Firebase API Key. Please check your google-services.json.";
        }
        
        if (lower.contains("configuration_not_found")) {
            return setupMessage();
        }

        if (lower.contains("password")) return "The password is incorrect or too weak.";
        if (lower.contains("email")) return "Check the email address and try again.";
        if (lower.contains("network")) return "Check your internet connection and try again.";

        return message;
    }
}
