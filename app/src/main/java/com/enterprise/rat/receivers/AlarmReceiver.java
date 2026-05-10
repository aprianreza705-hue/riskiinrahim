package com.enterprise.rat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.enterprise.rat.services.MainService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MainService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                context.startForegroundService(serviceIntent);
            } catch (Exception e) {}
        } else {
            context.startService(serviceIntent);
        }
    }
}
