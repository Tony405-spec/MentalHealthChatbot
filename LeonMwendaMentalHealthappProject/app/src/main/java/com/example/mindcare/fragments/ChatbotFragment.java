package com.example.mindcare.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import com.example.mindcare.adapters.ChatAdapter;
import com.example.mindcare.models.ChatMessage;
import com.example.mindcare.repository.ChatRepository;
import com.example.mindcare.utils.Prefs;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.Arrays;
import java.util.List;

public class ChatbotFragment extends Fragment {
    private final ChatRepository repo = new ChatRepository();
    private ChatAdapter adapter;
    private RecyclerView recycler;
    private View typing;

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.chatRecycler);
        typing = view.findViewById(R.id.typingText);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChatAdapter(repo.history());
        recycler.setAdapter(adapter);
        if (repo.history().isEmpty()) repo.add(new ChatMessage("How are you feeling today? Choose an intent above. I can offer grounding, reflection prompts, and practical next steps. I never provide diagnoses.", false));
        setupChips(view.findViewById(R.id.intentChips));
        adapter.notifyDataSetChanged();
    }

    private void setupChips(ChipGroup group) {
        List<String> intents = Arrays.asList("Stress", "Anxiety", "Depression", "Burnout", "Panic", "Overthinking", "Loneliness", "Academic Pressure", "Low Motivation", "Self-Esteem", "Sleep Problems", "Relationship Issues", "General Wellness");
        for (String intent : intents) {
            Chip chip = new Chip(requireContext());
            chip.setText(intent);
            chip.setCheckable(false);
            chip.setOnClickListener(v -> sendIntent(intent));
            group.addView(chip);
        }
    }

    private void sendIntent(String intent) {
        repo.add(new ChatMessage(intent, true));
        new Prefs(requireContext()).setLastIntent(intent);
        adapter.notifyItemInserted(repo.history().size() - 1);
        recycler.scrollToPosition(repo.history().size() - 1);
        typing.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String response = repo.isCrisis(intent) ? crisisMessage() : repo.responseFor(intent) + "\n\nQuick actions: breathe, journal, call support, or save a reminder.";
            repo.add(new ChatMessage(response, false));
            typing.setVisibility(View.GONE);
            adapter.notifyItemInserted(repo.history().size() - 1);
            recycler.scrollToPosition(repo.history().size() - 1);
        }, 850);
    }

    private String crisisMessage() {
        return "I am really glad you said something. If you may hurt yourself or someone else, call emergency services now. You can also contact Kenya Red Cross 1199, Befrienders Kenya +254 722 178 177, or the US 988 Lifeline. Please reach out to a trusted person nearby.";
    }
}
