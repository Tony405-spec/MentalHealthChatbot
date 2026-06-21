package com.example.mindcare.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {
    public static boolean validEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean validMpesaPhone(String phone) {
        return phone != null && phone.matches("2547\\d{8}");
    }
}
