package com.wellness.aichatbot;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wellness.aichatbot.data.UserSessionStore;
import com.wellness.aichatbot.payment.DonateActivity;
import com.wellness.aichatbot.theme.ThemeManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private UserSessionStore sessionStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        sessionStore = new UserSessionStore(this);
        
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null && !sessionStore.isLoggedIn()) {
            openLogin();
            return;
        }

        setContentView(R.layout.activity_main);
        ThemeManager.apply(this, findViewById(R.id.root));

        TextView greeting = findViewById(R.id.greetingText);
        String displayName = user != null ? user.getDisplayName() : sessionStore.getName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = sessionStore.getName();
        }
        greeting.setText(getString(greetingResId()) + ", " + displayName);

        TextView quoteText = findViewById(R.id.quoteText);
        quoteText.setText(getRandomQuote());

        MaterialCardView chatbotCard = findViewById(R.id.chatbotCard);
        MaterialCardView journalCard = findViewById(R.id.journalCard);
        MaterialCardView analyticsCard = findViewById(R.id.analyticsCard);
        MaterialCardView donateCard = findViewById(R.id.donateCard);
        TextView emergencyButton = findViewById(R.id.emergencyButton);
        emergencyButton.setTextColor(Color.WHITE);

        String helpline = getString(R.string.helpline);
        emergencyButton.setText(getString(R.string.emergency_support, helpline));

        chatbotCard.setOnClickListener(v -> startActivity(new Intent(this, ChatbotActivity.class)));
        donateCard.setOnClickListener(v -> startActivity(new Intent(this, DonateActivity.class)));
        findViewById(R.id.themeButton).setOnClickListener(v -> startActivity(new Intent(this, ThemeSettingsActivity.class)));
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            mAuth.signOut();
            sessionStore.logout();
            openLogin();
        });
        journalCard.setOnClickListener(v -> startActivity(new Intent(this, JournalActivity.class)));
        analyticsCard.setOnClickListener(v -> startActivity(new Intent(this, MoodAnalyticsActivity.class)));
        emergencyButton.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + helpline))));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (findViewById(R.id.root) != null) {
            ThemeManager.apply(this, findViewById(R.id.root));
            ((TextView) findViewById(R.id.emergencyButton)).setTextColor(Color.WHITE);
        }
    }

    private int greetingResId() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            return R.string.good_morning;
        }
        if (hour < 17) {
            return R.string.good_afternoon;
        }
        return R.string.good_evening;
    }

    private String getRandomQuote() {
        String[] quotes = getResources().getStringArray(R.array.wellness_quotes);
        if (quotes.length == 0) return getString(R.string.default_quote);
        return quotes[(int) (Math.random() * quotes.length)];
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
