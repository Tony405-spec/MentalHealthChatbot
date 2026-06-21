package com.example.mindcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mindcare.R;
import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.utils.Prefs;

public class SplashActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            boolean signedIn = FirebaseManager.get().auth().getCurrentUser() != null && new Prefs(this).isRememberMe();
            startActivity(new Intent(this, signedIn ? MainActivity.class : LoginActivity.class));
            finish();
        }, 1200);
    }
}
