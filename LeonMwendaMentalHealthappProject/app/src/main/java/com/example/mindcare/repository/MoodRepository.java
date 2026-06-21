package com.example.mindcare.repository;

import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.models.MoodEntry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MoodRepository {
    private CollectionReference moods() {
        return FirebaseManager.get().db().collection("moods");
    }

    public Task<Void> save(MoodEntry entry) {
        if (entry.getId() == null) entry.setId(moods().document().getId());
        return moods().document(entry.getId()).set(entry);
    }

    public Task<QuerySnapshot> listMine() {
        return moods().whereEqualTo("userId", FirebaseManager.get().uid()).orderBy("createdAt", Query.Direction.DESCENDING).get();
    }

    public Task<Void> delete(String id) {
        return moods().document(id).delete();
    }
}
