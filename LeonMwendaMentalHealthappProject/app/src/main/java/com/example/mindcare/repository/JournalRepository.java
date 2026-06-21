package com.example.mindcare.repository;

import com.example.mindcare.database.FirebaseManager;
import com.example.mindcare.models.JournalEntry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class JournalRepository {
    private CollectionReference journals() {
        return FirebaseManager.get().db().collection("journals");
    }

    public Task<Void> save(JournalEntry entry) {
        if (entry.getId() == null) entry.setId(journals().document().getId());
        return journals().document(entry.getId()).set(entry);
    }

    public Task<QuerySnapshot> listMine() {
        return journals().whereEqualTo("userId", FirebaseManager.get().uid()).orderBy("createdAt", Query.Direction.DESCENDING).get();
    }

    public Task<Void> delete(String id) {
        return journals().document(id).delete();
    }
}
