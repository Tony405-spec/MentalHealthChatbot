package com.example.mindcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mindcare.R;
import com.example.mindcare.activities.MainActivity;
import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.repository.MoodRepository;
import com.example.mindcare.repository.MpesaRepository;
import com.example.mindcare.utils.Prefs;
import com.example.mindcare.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        applyTheme(view);
        String email = FirebaseManager.get().auth().getCurrentUser() == null ? "friend" : FirebaseManager.get().auth().getCurrentUser().getEmail();
        ((TextView) view.findViewById(R.id.welcomeText)).setText("Welcome, " + email);
        ((TextView) view.findViewById(R.id.quoteText)).setText("Daily quote: You do not have to finish everything today to be worthy of rest.");
        new MoodRepository().listMine().addOnSuccessListener(snapshot -> {
            if (!snapshot.isEmpty()) ((TextView) view.findViewById(R.id.moodSummary)).setText("Latest check-in saved. You are building emotional awareness one note at a time.");
        });
        view.findViewById(R.id.quickMood).setOnClickListener(v -> ((MainActivity) requireActivity()).navigate(new MoodTrackerFragment()));
        view.findViewById(R.id.quickJournal).setOnClickListener(v -> ((MainActivity) requireActivity()).navigate(new JournalFragment()));
        view.findViewById(R.id.chatShortcut).setOnClickListener(v -> ((MainActivity) requireActivity()).navigate(new ChatbotFragment()));
        view.findViewById(R.id.supportShortcut).setOnClickListener(v -> ((MainActivity) requireActivity()).navigate(new EmergencySupportFragment()));
        view.findViewById(R.id.profileShortcut).setOnClickListener(v -> ((MainActivity) requireActivity()).navigate(new ProfileFragment()));
        view.findViewById(R.id.donateButton).setOnClickListener(v -> donate(view));
    }

    private void donate(View view) {
        TextInputEditText phoneInput = view.findViewById(R.id.donatePhone);
        TextInputEditText amountInput = view.findViewById(R.id.donateAmount);
        String phone = String.valueOf(phoneInput.getText()).trim();
        String amountText = String.valueOf(amountInput.getText()).trim();
        if (!ValidationUtils.validMpesaPhone(phone)) { phoneInput.setError("Use 2547XXXXXXXX"); return; }
        int amount = amountText.isEmpty() ? 0 : Integer.parseInt(amountText);
        if (amount < 1) { amountInput.setError("Enter an amount"); return; }
        new MpesaRepository().donate(phone, amount, new MpesaRepository.DonateCallback() {
            @Override public void onSuccess(String message) { requireActivity().runOnUiThread(() -> Snackbar.make(view, "M-Pesa prompt sent. Complete payment on your phone.", Snackbar.LENGTH_LONG).show()); }
            @Override public void onError(String message) { requireActivity().runOnUiThread(() -> Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()); }
        });
    }

    private void applyTheme(View view) {
        int bg = new Prefs(requireContext()).isGreyTheme() ? getResources().getColor(R.color.soft_grey) : getResources().getColor(R.color.white);
        view.findViewById(R.id.dashboardRoot).setBackgroundColor(bg);
    }
}
