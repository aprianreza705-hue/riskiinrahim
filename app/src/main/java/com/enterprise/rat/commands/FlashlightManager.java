package com.enterprise.rat.commands;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import com.enterprise.rat.utils.TelegramApi;

public class FlashlightManager {
    private Context context;
    public FlashlightManager(Context context) { this.context = context; }
    public void toggle(boolean on) {
        try {
            CameraManager cm = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String[] ids = cm.getCameraIdList();
            if (ids.length > 0) {
                cm.setTorchMode(ids[0], on);
                TelegramApi.sendMessage("🔦 Flashlight " + (on ? "ON" : "OFF"));
            } else {
                TelegramApi.sendMessage("❌ No camera found");
            }
        } catch (Exception e) { TelegramApi.sendMessage("❌ Flashlight error"); }
    }
}
