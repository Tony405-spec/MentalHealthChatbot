package com.wellness.aichatbot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wellness.aichatbot.data.UserAccountStore;
import com.wellness.aichatbot.data.UserSessionStore;
import com.wellness.aichatbot.theme.ThemeManager;

public class LoginActivity extends AppCompatActivity {
    private UserSessionStore sessionStore;
    private UserAccountStore accountStore;
    private boolean isLoginMode = true;

    private EditText nameInput, emailInput, passwordInput;
    private TextView loginButton, modeToggleText, errorText, loginSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionStore = new UserSessionStore(this);
        accountStore = new UserAccountStore(this);

        if (sessionStore.isLoggedIn()) {
            openDashboard();
            return;
        }

        setContentView(R.layout.activity_login);
        ThemeManager.apply(this, findViewById(R.id.loginRoot));

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        modeToggleText = findViewById(R.id.modeToggleText);
        errorText = findViewById(R.id.loginErrorText);
        loginSubtitle = findViewById(R.id.loginSubtitle);

        loginButton.setTextColor(Color.WHITE);

        updateUI();

        modeToggleText.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateUI();
        });

        loginButton.setOnClickListener(v -> handleAuth());
    }

    private void updateUI() {
        if (isLoginMode) {
            nameInput.setVisibility(View.GONE);
            loginSubtitle.setText(R.string.login_subtitle_signin);
            loginButton.setText(R.string.btn_login);
            modeToggleText.setText(R.string.toggle_new_user);
        } else {
            nameInput.setVisibility(View.VISIBLE);
            loginSubtitle.setText(R.string.login_subtitle_join);
            loginButton.setText(R.string.btn_create_account);
            modeToggleText.setText(R.string.toggle_existing_user);
        }
        errorText.setText("");
    }

    private void handleAuth() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (isLoginMode) {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                errorText.setText(R.string.err_login_fields);
                return;
            }
            String userName = accountStore.authenticate(email, password);
            if (userName != null) {
                sessionStore.login(userName, email);
                openDashboard();
            } else {
                errorText.setText(R.string.err_auth_failed);
            }
        } else {
            String error = validate(name, email, password);
            if (error != null) {
                errorText.setText(error);
                return;
            }

            if (accountStore.register(name, email, password)) {
                sessionStore.login(name, email);
                openDashboard();
            } else {
                errorText.setText(R.string.err_email_taken);
            }
        }
    }

    private String validate(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            return getString(R.string.err_enter_name);
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.err_invalid_email);
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return getString(R.string.err_invalid_password);
        }
        return null;
    }

    private void openDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
