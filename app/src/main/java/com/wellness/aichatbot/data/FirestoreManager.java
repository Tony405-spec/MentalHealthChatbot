package com.wellness.aichatbot.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FirestoreManager {
    private static final String USERS_COLLECTION = "users";
    private static final String CHAT_COLLECTION = "chats";
    private static final String JOURNAL_COLLECTION = "journals";

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private String getUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public void saveChatMessage(ChatMessage message) {
        String userId = getUserId();
        if (userId == null) return;

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(CHAT_COLLECTION)
                .add(message);
    }

    public void syncChatHistory(OnSyncCompleteListener listener) {
        String userId = getUserId();
        if (userId == null) {
            listener.onSyncComplete(Collections.emptyList());
            return;
        }

        CollectionReference chatRef = db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(CHAT_COLLECTION);

        chatRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ChatMessage> remoteMessages = queryDocumentSnapshots.toObjects(ChatMessage.class);
                    listener.onSyncComplete(remoteMessages);
                })
                .addOnFailureListener(e -> listener.onSyncComplete(Collections.emptyList()));
    }

    public void saveJournalEntry(String prompt, String content, OnSaveCompleteListener listener) {
        String userId = getUserId();
        if (userId == null) {
            listener.onComplete(false);
            return;
        }

        Map<String, Object> entry = new HashMap<>();
        entry.put("prompt", prompt);
        entry.put("content", content);
        entry.put("timestamp", System.currentTimeMillis());

        db.collection(USERS_COLLECTION)
                .document(userId)
                .collection(JOURNAL_COLLECTION)
                .add(entry)
                .addOnSuccessListener(documentReference -> listener.onComplete(true))
                .addOnFailureListener(e -> listener.onComplete(false));
    }

    public interface OnSyncCompleteListener {
        void onSyncComplete(List<ChatMessage> messages);
    }

    public interface OnSaveCompleteListener {
        void onComplete(boolean success);
    }
}
