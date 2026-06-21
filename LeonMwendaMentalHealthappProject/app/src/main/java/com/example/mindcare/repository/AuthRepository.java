package com.example.mindcare.repository;

import com.example.mindcare.database.FirebaseManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class AuthRepository {
    public Task<AuthResult> login(String email, String password) {
        return FirebaseManager.get().auth().signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return FirebaseManager.get().auth().createUserWithEmailAndPassword(email, password);
    }

    public void logout() {
        FirebaseManager.get().auth().signOut();
    }
}
