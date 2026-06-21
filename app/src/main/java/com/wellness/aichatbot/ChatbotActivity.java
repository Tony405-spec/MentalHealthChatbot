package com.wellness.aichatbot;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.wellness.aichatbot.chat.BotReply;
import com.wellness.aichatbot.chat.ChatEngine;
import com.wellness.aichatbot.chat.IntentChatEngine;
import com.wellness.aichatbot.data.ChatHistoryStore;
import com.wellness.aichatbot.data.ChatMessage;
import com.wellness.aichatbot.data.FirestoreManager;
import com.wellness.aichatbot.theme.ThemeManager;
import com.wellness.aichatbot.theme.ThemePalette;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ChatbotActivity extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ChatEngine chatEngine = new IntentChatEngine();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final Map<String, String[]> flowActions = new LinkedHashMap<>();

    private ChatHistoryStore historyStore;
    private FirestoreManager firestoreManager;
    private LinearLayout conversationContainer;
    private ChipGroup quickActionChipGroup;
    private ScrollView chatScroll;
    private EditText messageInput;
    private TextView typingText;
    private ThemePalette palette;
    private String selectedIntent = "General Wellness";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        ThemeManager.apply(this, findViewById(R.id.chatRoot));
        palette = ThemeManager.palette(this);

        historyStore = new ChatHistoryStore(this);
        firestoreManager = new FirestoreManager();
        conversationContainer = findViewById(R.id.conversationContainer);
        quickActionChipGroup = findViewById(R.id.quickActionChipGroup);
        chatScroll = findViewById(R.id.chatScroll);
        messageInput = findViewById(R.id.messageInput);
        typingText = findViewById(R.id.typingText);

        setupFlows();
        setupIntentChips();
        setupQuickActions("Help Me Relax", "Journal My Thoughts", "Breathing Exercise", "Mood Check-In", "Get Motivation", "Sleep Support");

        addBotCard("Welcome", "Choose what you are feeling above. I will remember the focus for this session and guide you with practical, supportive next steps.");
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.sendButton).setOnClickListener(v -> sendMessage());
    }

    private void setupFlows() {
        flowActions.put("Stress", new String[]{"School", "Work", "Family", "Relationships", "Finances", "Other"});
        flowActions.put("Anxiety", new String[]{"Breathing Exercise", "Grounding Technique", "Reassurance", "Journal Prompt"});
        flowActions.put("Depression", new String[]{"Self-Care Step", "Encouraging Support", "Professional Help", "Tiny Action"});
        flowActions.put("Burnout", new String[]{"Recovery Plan", "Boundary Script", "Rest Block", "Prioritize"});
        flowActions.put("Overthinking", new String[]{"Worry Window", "Reality Check", "Let It Rest", "Journal Prompt"});
        flowActions.put("Panic", new String[]{"4-6 Breathing", "Ground My Body", "Name The Room", "Emergency Support"});
        flowActions.put("Loneliness", new String[]{"Reach Out", "Self-Compassion", "Connection Plan", "Journal Prompt"});
        flowActions.put("Low Motivation", new String[]{"Five-Minute Start", "Tiny Goal", "Encouragement", "Remove Friction"});
        flowActions.put("Self-Esteem", new String[]{"Kind Self-Talk", "Strength List", "Reframe Thought", "Confidence Prompt"});
        flowActions.put("Sleep Problems", new String[]{"Sleep Hygiene", "Relaxation Routine", "Bedtime Checklist", "Park Worries"});
        flowActions.put("Academic Pressure", new String[]{"Study Planning", "Focus Strategy", "Ask For Help", "Burnout Prevention"});
        flowActions.put("Relationship Issues", new String[]{"Boundary", "Calm Script", "Reflect Needs", "Repair Conversation"});
        flowActions.put("Anger", new String[]{"Pause Plan", "Cool Down", "Name The Need", "Repair Step"});
        flowActions.put("Grief", new String[]{"Gentle Ritual", "Memory Prompt", "Support Person", "Permission To Feel"});
        flowActions.put("General Wellness", new String[]{"Mood Check-In", "Mindfulness", "Gratitude", "Daily Reset"});
    }

    private void setupIntentChips() {
        ChipGroup group = findViewById(R.id.intentChipGroup);
        for (String intent : flowActions.keySet()) {
            Chip chip = makeChip(intent);
            chip.setOnClickListener(v -> beginIntentFlow(intent));
            group.addView(chip);
        }
    }

    private Chip makeChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setTextColor(palette.text);
        chip.setChipBackgroundColorResource(R.color.blanc_white);
        chip.setChipStrokeWidth(1);
        chip.setChipStrokeColorResource(R.color.alabaster);
        chip.setCheckable(false);
        chip.setMinHeight(dp(44));
        return chip;
    }

    private void beginIntentFlow(String intent) {
        selectedIntent = intent;
        addUserCard(intent, "Selected focus");
        showTypingThen(() -> {
            addBotCard(intent + " Support", introFor(intent) + "\n\nCan you tell me what is contributing most right now?");
            setupQuickActions(flowActions.get(intent));
        });
    }

    private String introFor(String intent) {
        if ("Stress".equals(intent)) {
            return "I hear that you're feeling stressed. Let's work through it together with one steady step at a time.";
        }
        if ("Panic".equals(intent)) {
            return "Panic can feel intense and frightening. You are not alone in this moment; let's slow the body first.";
        }
        if ("Depression".equals(intent)) {
            return "I'm sorry things feel heavy. I can offer support and small care steps, but I cannot diagnose.";
        }
        return "Thank you for naming " + intent.toLowerCase(Locale.US) + ". That awareness is a meaningful first step.";
    }

    private void setupQuickActions(String... actions) {
        quickActionChipGroup.removeAllViews();
        if (actions == null) {
            return;
        }
        for (String action : actions) {
            Chip chip = makeChip(action);
            chip.setOnClickListener(v -> handleQuickAction(action));
            quickActionChipGroup.addView(chip);
        }
    }

    private void handleQuickAction(String action) {
        addUserCard(action, selectedIntent);
        showTypingThen(() -> addBotCard(action, responseForAction(action)));
    }

    private String responseForAction(String action) {
        String lower = action.toLowerCase(Locale.US);
        if (lower.contains("breath") || lower.contains("relax")) {
            return "Exercise card\n\nInhale for 4 counts. Hold for 2. Exhale for 6. Repeat five times while softening your jaw and shoulders.";
        }
        if (lower.contains("ground")) {
            return "Grounding card\n\nName 5 things you see, 4 you feel, 3 you hear, 2 you smell, and 1 thing you can taste.";
        }
        if (lower.contains("journal")) {
            return "Journal prompt\n\nWhat is the strongest feeling here, what is it asking for, and what would a kind next step look like?";
        }
        if (lower.contains("emergency") || lower.contains("professional")) {
            return crisisCard();
        }
        if (lower.contains("sleep") || lower.contains("bedtime")) {
            return "Sleep support card\n\nDim lights, put your phone away, write down tomorrow's worries, and do a slow body scan from forehead to feet.";
        }
        if (lower.contains("study") || lower.contains("focus")) {
            return "Focus card\n\nPick one task, set a 25-minute timer, remove one distraction, and write the exact first action before starting.";
        }
        return "Tip card\n\nTry one small action you can complete in two minutes. Small regulation steps are often more helpful than forcing a big fix.";
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        addUserCard(text, selectedIntent);
        ChatMessage userMessage = new ChatMessage(ChatMessage.SENDER_USER, text, System.currentTimeMillis());
        historyStore.append(userMessage);
        firestoreManager.saveChatMessage(userMessage);
        messageInput.setText("");

        showTypingThen(() -> {
            BotReply reply = chatEngine.replyTo(text);
            String title = reply.isCrisis() ? "Emergency Support" : selectedIntent + " Guidance";
            addBotCard(title, reply.getText());
            ChatMessage botMessage = new ChatMessage(ChatMessage.SENDER_BOT, reply.getText(), System.currentTimeMillis());
            historyStore.append(botMessage);
            firestoreManager.saveChatMessage(botMessage);
            if (reply.isCrisis()) {
                setupQuickActions("Call Helpline", "Find Safe Person", "Emergency Support");
            }
        });
    }

    private String crisisCard() {
        return "Emergency support card\n\nIf you might hurt yourself or someone else, call the mental health helpline now: +254 722 178177.\n\nIf there is immediate danger, contact local emergency services or go to the nearest hospital.";
    }

    private void showTypingThen(Runnable runnable) {
        typingText.setText("Mindful Support is typing • • •");
        typingText.setVisibility(View.VISIBLE);
        handler.postDelayed(() -> {
            typingText.setVisibility(View.GONE);
            runnable.run();
        }, 650);
    }

    private void addBotCard(String title, String body) {
        addMessageCard(title, body, false);
    }

    private void addUserCard(String body, String title) {
        addMessageCard(title, body, true);
    }

    private void addMessageCard(String title, String body, boolean user) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                user ? (int) (getResources().getDisplayMetrics().widthPixels * 0.78f) : LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(user ? dp(44) : 0, dp(6), user ? 0 : dp(20), dp(8));
        card.setLayoutParams(cardParams);
        card.setRadius(dp(20));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(user ? Color.rgb(18, 18, 18) : palette.card);
        card.setAlpha(0f);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(14), dp(16), dp(14));

        TextView titleView = new TextView(this);
        titleView.setText(title + " • " + timeFormat.format(new Date()));
        titleView.setTextSize(11);
        titleView.setTextColor(user ? Color.WHITE : palette.secondary);
        content.addView(titleView);

        TextView bodyView = new TextView(this);
        bodyView.setText(body);
        bodyView.setTextSize(14);
        bodyView.setLineSpacing(dp(2), 1.05f);
        bodyView.setTextColor(user ? Color.WHITE : palette.text);
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bodyParams.setMargins(0, dp(6), 0, 0);
        bodyView.setLayoutParams(bodyParams);
        content.addView(bodyView);

        card.addView(content);
        LinearLayout row = new LinearLayout(this);
        row.setGravity(user ? Gravity.END : Gravity.START);
        row.addView(card);
        conversationContainer.addView(row);
        card.animate().alpha(1f).translationY(0).setDuration(220).start();
        scrollToBottom();
    }

    private void scrollToBottom() {
        chatScroll.post(() -> chatScroll.smoothScrollTo(0, conversationContainer.getBottom()));
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
