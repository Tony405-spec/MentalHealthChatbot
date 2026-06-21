package com.example.mindcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import com.example.mindcare.adapters.SimpleCardAdapter;
import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.models.JournalEntry;
import com.example.mindcare.repository.JournalRepository;
import com.example.mindcare.utils.TimeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class JournalFragment extends Fragment {
    private final JournalRepository repo = new JournalRepository();
    private final List<JournalEntry> entries = new ArrayList<>();
    private SimpleCardAdapter adapter;

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_fab, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((android.widget.TextView) view.findViewById(R.id.screenTitle)).setText("Journal");
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
                JournalEntry e = doc.toObject(JournalEntry.class);
                e.setId(doc.getId());
                entries.add(e);
            }
            List<String> titles = new ArrayList<>(), subtitles = new ArrayList<>();
            for (JournalEntry e : entries) { titles.add(e.getType()); subtitles.add(TimeUtils.pretty(e.getCreatedAt()) + "\n" + e.getContent()); }
            adapter.setItems(titles, subtitles);
        });
    }

    private void showEditor(@Nullable JournalEntry existing) {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 10, 40, 0);
        Spinner type = new Spinner(requireContext());
        String[] types = {"Free writing", "Gratitude journal", "Daily reflection", "Mood reflection"};
        type.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, types));
        EditText content = new EditText(requireContext());
        content.setHint("Write without judgment...");
        content.setMinLines(5);
        layout.addView(type);
        layout.addView(content);
        if (existing != null) {
            content.setText(existing.getContent());
            for (int i = 0; i < types.length; i++) if (types[i].equals(existing.getType())) type.setSelection(i);
        }
        new MaterialAlertDialogBuilder(requireContext()).setTitle(existing == null ? "New journal" : "Edit journal").setView(layout)
                .setPositiveButton("Save", (d, w) -> {
                    JournalEntry entry = existing == null ? new JournalEntry(FirebaseManager.get().uid(), "", "") : existing;
                    entry.setType(type.getSelectedItem().toString());
                    entry.setContent(content.getText().toString());
                    repo.save(entry).addOnSuccessListener(x -> load());
                }).setNegativeButton("Cancel", null).show();
    }

    private void confirmDelete(JournalEntry entry) {
        new MaterialAlertDialogBuilder(requireContext()).setTitle("Delete journal entry?").setMessage("This cannot be undone.")
                .setPositiveButton("Delete", (d, w) -> repo.delete(entry.getId()).addOnSuccessListener(x -> load()))
                .setNegativeButton("Cancel", null).show();
    }
}
