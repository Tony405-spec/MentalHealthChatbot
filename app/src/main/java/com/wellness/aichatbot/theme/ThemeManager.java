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
        
        Typeface font = null;
        try {
            font = ResourcesCompat.getFont(activity, R.font.roboto_mono);
        } catch (Exception ignored) {}

        applyToChildren(root, palette, font);
        Window window = activity.getWindow();
        window.setStatusBarColor(palette.background);
        window.setNavigationBarColor(palette.background);
        new WindowInsetsControllerCompat(window, window.getDecorView()).setAppearanceLightStatusBars(!palette.dark);
    }

    private static void applyToChildren(View view, ThemePalette palette, Typeface font) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(palette.text);
            if (font != null) tv.setTypeface(font);
        }
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setTextColor(Color.rgb(18, 18, 18));
            editText.setHintTextColor(Color.rgb(96, 96, 96));
            if (font != null) editText.setTypeface(font);
        }
        if (view instanceof MaterialCardView) {
            MaterialCardView card = (MaterialCardView) view;
            card.setCardBackgroundColor(palette.card);
            card.setStrokeColor(palette.secondary);
            card.setStrokeWidth(1);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyToChildren(group.getChildAt(i), palette, font);
            }
        }
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
