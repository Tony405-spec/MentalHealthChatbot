package com.wellness.aichatbot;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wellness.aichatbot.data.FirestoreManager;
import com.wellness.aichatbot.theme.ThemeManager;
import com.wellness.aichatbot.theme.ThemePalette;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class JournalActivity extends AppCompatActivity {
    private static final String PREF_NAME = "journal_stats";
    private final Map<String, String> templates = new LinkedHashMap<>();
    private FirestoreManager firestoreManager;
    private ThemePalette palette;
    private String currentTemplate = "Daily Reflection";
    private TextView templateTitle;
    private TextView promptText;
    private EditText journalInput;
    private LinearLayout insightsContainer;
    private LinearLayout timelineContainer;
    private SharedPreferences stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        ThemeManager.apply(this, findViewById(R.id.journalRoot));
        palette = ThemeManager.palette(this);
        firestoreManager = new FirestoreManager();
        stats = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        templateTitle = findViewById(R.id.templateTitle);
        promptText = findViewById(R.id.promptText);
        journalInput = findViewById(R.id.journalInput);
        insightsContainer = findViewById(R.id.insightsContainer);
        timelineContainer = findViewById(R.id.timelineContainer);

        ((TextView) findViewById(R.id.dateText)).setText(new SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(new Date()));
        setupTemplates();
        setupActionChips();
        renderInsights();
        renderTimeline();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setTextColor(Color.WHITE);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveEntry(saveButton));
    }

    private void setupTemplates() {
        templates.put("Free Write", "What feels important to write down right now?");
        templates.put("Gratitude Journal", "What are 3 things you're grateful for?\nWho made your day better?\nWhat positive moment stood out today?");
        templates.put("Daily Reflection", "What went well today?\nWhat challenged you?\nWhat did you learn?");
        templates.put("Mood Reflection", "How do you feel today?\nWhat triggered this emotion?\nWhat would help improve your mood?");
        templates.put("Goal Reflection", "What goal matters most right now?\nWhat is one small next step?\nWhat support would make it easier?");
        applyTemplate(currentTemplate);
    }

    private void setupActionChips() {
        ChipGroup group = findViewById(R.id.journalActionGroup);
        for (String name : templates.keySet()) {
            Chip chip = new Chip(this);
            chip.setText(name);
            chip.setCheckable(true);
            chip.setMinHeight(dp(44));
            chip.setChipBackgroundColorResource(R.color.blanc_white);
            chip.setChipStrokeColorResource(R.color.alabaster);
            chip.setChipStrokeWidth(1);
            chip.setOnClickListener(v -> applyTemplate(name));
            group.addView(chip);
            if (name.equals(currentTemplate)) {
                chip.setChecked(true);
            }
        }
    }

    private void applyTemplate(String name) {
        currentTemplate = name;
        if (templateTitle != null) {
            templateTitle.setText(name);
            promptText.setText(templates.get(name));
        }
    }

    private void saveEntry(Button saveButton) {
        String text = journalInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Write something before saving.", Toast.LENGTH_SHORT).show();
            return;
        }
        saveButton.setEnabled(false);
        firestoreManager.saveJournalEntry(currentTemplate, text, success -> {
            saveButton.setEnabled(true);
            incrementStats(text);
            addTimelineCard("Saved just now", currentTemplate, text, "Calm");
            journalInput.setText("");
            renderInsights();
            Toast.makeText(this, success ? "Reflection saved." : "Saved locally. Cloud sync can retry later.", Toast.LENGTH_LONG).show();
        });
    }

    private void incrementStats(String text) {
        int total = stats.getInt("total", 2) + 1;
        int weekly = stats.getInt("weekly", 2) + 1;
        int streak = stats.getInt("streak", 1) + 1;
        stats.edit()
                .putInt("total", total)
                .putInt("weekly", weekly)
                .putInt("streak", streak)
                .putString("last_preview", text)
                .putString("last_template", currentTemplate)
                .apply();
    }

    private void renderInsights() {
        insightsContainer.removeAllViews();
        addInsight("Total Entries", String.valueOf(stats.getInt("total", 2)));
        addInsight("Common Mood", "Calm");
        addInsight("Streak", stats.getInt("streak", 1) + " days");
    }

    private void addInsight(String label, String value) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(dp(3), 0, dp(3), 0);
        card.setLayoutParams(params);
        card.setRadius(dp(18));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(palette.card);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(12), dp(12), dp(12), dp(12));
        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextSize(18);
        valueView.setTextColor(palette.text);
        valueView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setTextSize(10);
        labelView.setTextColor(palette.secondary);
        body.addView(valueView);
        body.addView(labelView);
        card.addView(body);
        insightsContainer.addView(card);
    }

    private void renderTimeline() {
        timelineContainer.removeAllViews();
        String preview = stats.getString("last_preview", "A quiet reflection on what felt steady and what needed care.");
        String title = stats.getString("last_template", "Daily Reflection");
        addTimelineCard("Today", title, preview, "Calm");
        addTimelineCard("Yesterday", "Gratitude Journal", "Three small moments of gratitude and a kind message from a friend.", "Grateful");
    }

    private void addTimelineCard(String date, String title, String preview, String mood) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(params);
        card.setRadius(dp(20));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(palette.card);

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(16), dp(14), dp(16), dp(14));
        TextView heading = new TextView(this);
        heading.setText(moodIcon(mood) + "  " + title);
        heading.setTextSize(16);
        heading.setTextColor(palette.text);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        TextView meta = new TextView(this);
        meta.setText(date + " • Mood: " + mood);
        meta.setTextSize(11);
        meta.setTextColor(palette.secondary);
        TextView previewView = new TextView(this);
        previewView.setText(preview.length() > 120 ? preview.substring(0, 117) + "..." : preview);
        previewView.setTextSize(13);
        previewView.setTextColor(palette.text);
        previewView.setPadding(0, dp(8), 0, 0);
        body.addView(heading);
        body.addView(meta);
        body.addView(previewView);
        card.addView(body);
        timelineContainer.addView(card, 0);
    }

    private String moodIcon(String mood) {
        if ("Grateful".equals(mood)) {
            return "+";
        }
        return "~";
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
