package com.example.mindcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mindcare.R;
import com.example.mindcare.repository.AuthRepository;
import com.example.mindcare.utils.FirebaseConfigUtils;
import com.example.mindcare.utils.Prefs;
import com.example.mindcare.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private CheckBox rememberCheck;
    private View progress;
    private MaterialButton primaryButton;
    private final AuthRepository auth = new AuthRepository();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        ((TextView) findViewById(R.id.authTitle)).setText("Welcome back");
        ((TextView) findViewById(R.id.authSubtitle)).setText("Sign in to continue your care journey.");
        
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberCheck = findViewById(R.id.rememberCheck);
        progress = findViewById(R.id.progress);
        primaryButton = findViewById(R.id.primaryButton);
        MaterialButton secondaryButton = findViewById(R.id.secondaryButton);

        primaryButton.setText("Sign in");
        secondaryButton.setText("Create account");

        primaryButton.setOnClickListener(v -> login());
        secondaryButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        if (!FirebaseConfigUtils.hasValidFirebaseApiKey(this)) {
            Snackbar.make(emailInput, FirebaseConfigUtils.setupMessage(), Snackbar.LENGTH_LONG).show();
            return;
        }

        String email = String.valueOf(emailInput.getText()).trim();
        String password = String.valueOf(passwordInput.getText());

        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (!ValidationUtils.validEmail(email)) {
            emailLayout.setError("Enter a valid email");
            return;
        }
        if (!ValidationUtils.validPassword(password)) {
            passwordLayout.setError("Use at least 6 characters");
            return;
        }

        setLoading(true);
        auth.login(email, password).addOnSuccessListener(result -> {
            new Prefs(this).setRememberMe(rememberCheck.isChecked());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            setLoading(false);
            Snackbar.make(emailInput, FirebaseConfigUtils.friendlyAuthError(e), Snackbar.LENGTH_LONG).show();
        });
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
        primaryButton.setEnabled(!loading);
    }
}
