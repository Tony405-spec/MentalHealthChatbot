package com.wellness.aichatbot.theme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.Window;
import android.widget.TextView;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import com.wellness.aichatbot.R;

import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.card.MaterialCardView;

public class ThemeManager {
    private static final String PREF_NAME = "mindful_theme";
    private static final String KEY_THEME = "theme";

    public static AppThemeMode getTheme(Context context) {
        String value = prefs(context).getString(KEY_THEME, AppThemeMode.CREAM.name());
        return AppThemeMode.valueOf(value);
    }

    public static void setTheme(Context context, AppThemeMode mode) {
        prefs(context).edit().putString(KEY_THEME, mode.name()).apply();
    }

    public static ThemePalette palette(Context context) {
        switch (getTheme(context)) {
            case LIGHT:
                return new ThemePalette(Color.WHITE, Color.rgb(244, 246, 248), Color.rgb(18, 18, 18), Color.rgb(96, 125, 139), Color.rgb(102, 119, 97), false);
            case DARK:
                return new ThemePalette(Color.rgb(18, 18, 18), Color.rgb(30, 30, 30), Color.WHITE, Color.rgb(216, 210, 200), Color.rgb(141, 176, 154), true);
            case CREAM:
            default:
                return new ThemePalette(Color.rgb(216, 210, 200), Color.WHITE, Color.rgb(18, 18, 18), Color.rgb(102, 119, 97), Color.rgb(157, 107, 83), false);
        }
    }

    @SuppressWarnings("deprecation")
    public static void apply(Activity activity, View root) {
        ThemePalette palette = palette(activity);
        root.setBackgroundColor(palette.background);
        
        Typeface fontRegular = null;
        Typeface fontLight = null;
        try {
            fontRegular = ResourcesCompat.getFont(activity, R.font.roboto_mono);
            fontLight = ResourcesCompat.getFont(activity, R.font.roboto_mono_light);
        } catch (Exception ignored) {}

        boolean isDashboard = activity.getClass().getSimpleName().equals("MainActivity");

        applyToChildren(root, palette, fontRegular, fontLight, isDashboard);
        Window window = activity.getWindow();
        window.setStatusBarColor(palette.background);
        window.setNavigationBarColor(palette.background);
        new WindowInsetsControllerCompat(window, window.getDecorView()).setAppearanceLightStatusBars(!palette.dark);
    }

    private static void applyToChildren(View view, ThemePalette palette, Typeface fontRegular, Typeface fontLight, boolean isDashboard) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(palette.text);
            
            Typeface current = tv.getTypeface();
            boolean isBold = current != null && (current.isBold() || tv.getTextSize() > 20);
            
            if (isBold) {
                if (fontRegular != null) tv.setTypeface(fontRegular, Typeface.BOLD);
            } else if (isDashboard) {
                if (fontRegular != null) tv.setTypeface(fontRegular, Typeface.NORMAL);
            } else {
                if (fontLight != null) tv.setTypeface(fontLight);
            }
        }
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setTextColor(Color.rgb(18, 18, 18));
            editText.setHintTextColor(Color.rgb(120, 120, 120));
            if (fontLight != null) editText.setTypeface(fontLight);
        }
        if (view instanceof MaterialCardView) {
            MaterialCardView card = (MaterialCardView) view;
            card.setCardBackgroundColor(palette.card);
            card.setStrokeColor(palette.secondary);
            card.setStrokeWidth(2); // Slightly thicker professional border
            card.setRadius(dp(view.getContext(), 4)); // Sharp IBM-style corners
            card.setCardElevation(0); // Flat design for IBM feel
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyToChildren(group.getChildAt(i), palette, fontRegular, fontLight, isDashboard);
            }
        }
    }

    private static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
