package com.enterprise.rat.commands;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import com.enterprise.rat.utils.TelegramApi;

public class SystemSettingsManager {
    private Context context;
    public SystemSettingsManager(Context context) { this.context = context; }
    public void setVolume(int level) { AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); am.setStreamVolume(AudioManager.STREAM_RING, Math.min(Math.max(level,0), am.getStreamMaxVolume(AudioManager.STREAM_RING)), 0); TelegramApi.sendMessage("🔊 Volume set."); }
    public void setBrightness(int level) { Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, Math.min(Math.max(level,0), 255)); TelegramApi.sendMessage("💡 Brightness set."); }
    public void setRingMode(String mode) { AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); switch(mode) { case "silent": am.setRingerMode(AudioManager.RINGER_MODE_SILENT); break; case "vibrate": am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); break; default: am.setRingerMode(AudioManager.RINGER_MODE_NORMAL); } TelegramApi.sendMessage("🔔 Ring mode: " + mode); }
}
