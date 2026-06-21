package com.example.mindcare.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.mindcare.R;
import com.example.mindcare.fragments.ArticlesFragment;
import com.example.mindcare.fragments.ChatbotFragment;
import com.example.mindcare.fragments.DashboardFragment;
import com.example.mindcare.fragments.JournalFragment;
import com.example.mindcare.fragments.MoodTrackerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.nav_mood) fragment = new MoodTrackerFragment();
            else if (id == R.id.nav_journal) fragment = new JournalFragment();
            else if (id == R.id.nav_articles) fragment = new ArticlesFragment();
            else if (id == R.id.nav_chat) fragment = new ChatbotFragment();
            else fragment = new DashboardFragment();
            navigate(fragment);
            return true;
        });
        nav.setSelectedItemId(R.id.nav_dashboard);
    }

    public void navigate(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
