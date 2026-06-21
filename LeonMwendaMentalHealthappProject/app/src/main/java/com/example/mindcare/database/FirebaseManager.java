package com.example.mindcare.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {
    private static FirebaseManager instance;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseManager() { }

    public static synchronized FirebaseManager get() {
        if (instance == null) instance = new FirebaseManager();
        return instance;
    }

    public FirebaseAuth auth() { return auth; }
    public FirebaseFirestore db() { return db; }
    public String uid() { return auth.getCurrentUser() == null ? "" : auth.getCurrentUser().getUid(); }
}
