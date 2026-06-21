package com.example.mindcare.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static String pretty(long millis) {
        return new SimpleDateFormat("EEE, MMM d - HH:mm", Locale.getDefault()).format(new Date(millis));
    }
}
