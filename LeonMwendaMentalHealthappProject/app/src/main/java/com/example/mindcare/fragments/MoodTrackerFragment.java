package com.example.mindcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import com.example.mindcare.adapters.SimpleCardAdapter;
import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.models.MoodEntry;
import com.example.mindcare.repository.MoodRepository;
import com.example.mindcare.utils.TimeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class MoodTrackerFragment extends Fragment {
    private final MoodRepository repo = new MoodRepository();
    private final List<MoodEntry> entries = new ArrayList<>();
    private SimpleCardAdapter adapter;

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_fab, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((android.widget.TextView) view.findViewById(R.id.screenTitle)).setText("Mood Tracker");
        adapter = new SimpleCardAdapter();
        RecyclerView recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);
        adapter.setClickListener(new SimpleCardAdapter.ClickListener() {
            @Override public void onClick(int position) { showEditor(entries.get(position)); }
            @Override public void onLongClick(int position) { confirmDelete(entries.get(position)); }
        });
        view.findViewById(R.id.fab).setOnClickListener(v -> showEditor(null));
        load();
    }

    private void load() {
        repo.listMine().addOnSuccessListener(snapshot -> {
            entries.clear();
            for (com.google.firebase.firestore.QueryDocumentSnapshot doc : snapshot) {
                MoodEntry e = doc.toObject(MoodEntry.class);
                e.setId(doc.getId());
                entries.add(e);
            }
            List<String> titles = new ArrayList<>(), subtitles = new ArrayList<>();
            for (MoodEntry e : entries) { titles.add(e.getMood()); subtitles.add(TimeUtils.pretty(e.getCreatedAt()) + "\n" + e.getNotes()); }
            adapter.setItems(titles, subtitles);
        });
    }

    private void showEditor(@Nullable MoodEntry existing) {
        View form = LayoutInflater.from(requireContext()).inflate(android.R.layout.simple_list_item_2, null);
        EditText mood = new EditText(requireContext());
        mood.setHint("Mood e.g. Calm, Anxious, Tired");
        EditText notes = new EditText(requireContext());
        notes.setHint("Notes");
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 10, 40, 0);
        layout.addView(mood);
        layout.addView(notes);
        if (existing != null) { mood.setText(existing.getMood()); notes.setText(existing.getNotes()); }
        new MaterialAlertDialogBuilder(requireContext()).setTitle(existing == null ? "New mood" : "Edit mood").setView(layout)
                .setPositiveButton("Save", (d, w) -> {
                    MoodEntry entry = existing == null ? new MoodEntry(FirebaseManager.get().uid(), "", "") : existing;
                    entry.setMood(mood.getText().toString());
                    entry.setNotes(notes.getText().toString());
                    repo.save(entry).addOnSuccessListener(x -> load());
                }).setNegativeButton("Cancel", null).show();
    }

    private void confirmDelete(MoodEntry entry) {
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Delete mood entry?").setMessage("This cannot be undone.")
                .setPositiveButton("Delete", (d, w) -> repo.delete(entry.getId()).addOnSuccessListener(x -> load()))
                .setNegativeButton("Cancel", null).show();
    }
}
