package com.example.mindcare.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.mindcare.R;
import com.example.mindcare.activities.LoginActivity;
import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.notifications.ReminderScheduler;
import com.example.mindcare.repository.AuthRepository;
import com.example.mindcare.utils.Prefs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {
    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Prefs prefs = new Prefs(requireContext());
        view.findViewById(R.id.profileRoot).setBackgroundColor(prefs.isGreyTheme() ? getResources().getColor(R.color.soft_grey) : getResources().getColor(R.color.white));
        String email = FirebaseManager.get().auth().getCurrentUser() == null ? "Signed in user" : FirebaseManager.get().auth().getCurrentUser().getEmail();
        ((TextView) view.findViewById(R.id.emailText)).setText(email);
        Switch themeSwitch = view.findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(prefs.isGreyTheme());
        themeSwitch.setOnCheckedChangeListener((buttonView, checked) -> {
            prefs.setGreyTheme(checked);
            Snackbar.make(view, "Theme saved. Reopen the screen to see the update.", Snackbar.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.reminderButton).setOnClickListener(v -> showReminderDialog(view));
        view.findViewById(R.id.logoutButton).setOnClickListener(v -> {
            prefs.clearSession();
            new AuthRepository().logout();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finishAffinity();
        });
    }

    private void showReminderDialog(View view) {
        String[] types = {"Meditation", "Sleep", "Medication", "Journaling"};
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Reminder type").setItems(types, (dialog, which) -> {
            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 44);
            }
            new ReminderScheduler().schedule(requireContext(), types[which], System.currentTimeMillis() + 60 * 60 * 1000);
            Snackbar.make(view, types[which] + " reminder scheduled for one hour from now.", Snackbar.LENGTH_LONG).show();
        }).show();
    }
}
