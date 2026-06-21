package com.wellness.aichatbot.theme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.wellness.aichatbot.R;

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
                return new ThemePalette(Color.WHITE, Color.rgb(244, 246, 248), Color.rgb(18, 18, 18), Color.rgb(0, 102, 204), Color.rgb(102, 119, 97), false);
            case DARK:
                return new ThemePalette(Color.rgb(18, 18, 18), Color.rgb(30, 30, 30), Color.WHITE, Color.rgb(0, 102, 255), Color.rgb(141, 176, 154), true);
            case CREAM:
            default:
                return new ThemePalette(Color.rgb(216, 210, 200), Color.WHITE, Color.rgb(18, 18, 18), Color.rgb(0, 80, 158), Color.rgb(157, 107, 83), false);
        }
    }

    public static void apply(Activity activity, View root) {
        ThemePalette palette = palette(activity);
        root.setBackgroundColor(palette.background);
        
        Typeface fontRegular = null;
        Typeface fontLight = null;
        try {
            fontRegular = ResourcesCompat.getFont(activity, R.font.roboto_mono);
            fontLight = ResourcesCompat.getFont(activity, R.font.roboto_mono_light);
        } catch (Exception ignored) {}

        applyToChildren(root, palette, fontRegular, fontLight);
        
        Window window = activity.getWindow();
        window.setStatusBarColor(palette.background);
        window.setNavigationBarColor(palette.background);
        new WindowInsetsControllerCompat(window, window.getDecorView()).setAppearanceLightStatusBars(!palette.dark);
    }

    private static void applyToChildren(View view, ThemePalette palette, Typeface reg, Typeface light) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(palette.text);
            
            // Apply Mono font strictly
            if (tv.getTypeface() != null && tv.getTypeface().isBold()) {
                if (reg != null) tv.setTypeface(reg, Typeface.BOLD);
            } else {
                if (light != null) tv.setTypeface(light);
                else if (reg != null) tv.setTypeface(reg);
            }
        }
        
        if (view instanceof EditText) {
            EditText et = (EditText) view;
            et.setTextColor(palette.text);
            et.setHintTextColor(Color.GRAY);
            if (light != null) et.setTypeface(light);
            else if (reg != null) et.setTypeface(reg);
        }

        if (view instanceof MaterialCardView) {
            MaterialCardView card = (MaterialCardView) view;
            card.setCardBackgroundColor(palette.card);
            card.setStrokeColor(palette.secondary);
            card.setStrokeWidth(dp(view.getContext(), 2));
            card.setRadius(dp(view.getContext(), 2)); // Sharp technical corners
            card.setCardElevation(0);
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyToChildren(group.getChildAt(i), palette, reg, light);
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
