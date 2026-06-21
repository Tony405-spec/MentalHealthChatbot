package com.wellness.aichatbot;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.wellness.aichatbot.theme.AppThemeMode;
import com.wellness.aichatbot.theme.ThemeManager;

public class ThemeSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_settings);
        ThemeManager.apply(this, findViewById(R.id.themeRoot));

        RadioGroup group = findViewById(R.id.themeGroup);
        AppThemeMode current = ThemeManager.getTheme(this);
        if (current == AppThemeMode.LIGHT) {
            group.check(R.id.lightTheme);
        } else if (current == AppThemeMode.DARK) {
            group.check(R.id.darkTheme);
        } else {
            group.check(R.id.creamTheme);
        }

        group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.lightTheme) {
                ThemeManager.setTheme(this, AppThemeMode.LIGHT);
            } else if (checkedId == R.id.darkTheme) {
                ThemeManager.setTheme(this, AppThemeMode.DARK);
            } else {
                ThemeManager.setTheme(this, AppThemeMode.CREAM);
            }
            ThemeManager.apply(this, findViewById(R.id.themeRoot));
        });
    }
}
