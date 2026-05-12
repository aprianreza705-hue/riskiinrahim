package com.enterprise.rat.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.enterprise.rat.services.KeepAliveJobService;
import com.enterprise.rat.receivers.AlarmReceiver;

public class PersistenceManager {

    private static final int KEEP_ALIVE_JOB_ID = 999;
    private static final int ALARM_REQUEST_CODE = 888;

    public static void scheduleKeepAlive(Context context) {
        // 1. JobScheduler (API 21+, persists across reboots)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ComponentName component = new ComponentName(context, KeepAliveJobService.class);
            JobInfo jobInfo = new JobInfo.Builder(KEEP_ALIVE_JOB_ID, component)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true) // Survive reboot
                .setPeriodic(15 * 60 * 1000) // 15 minutes minimum
                .setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                .build();
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (scheduler != null) {
                scheduler.schedule(jobInfo);
            }
        }

        // 2. AlarmManager watchdog (every 5 minutes)
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM_REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (5 * 60 * 1000),
                        pendingIntent);
                } else {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (5 * 60 * 1000),
                        5 * 60 * 1000, pendingIntent);
                }
            } catch (SecurityException e) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (5 * 60 * 1000), pendingIntent);
            }
        }
    }

    public static void cancelKeepAlive(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (scheduler != null) scheduler.cancel(KEEP_ALIVE_JOB_ID);
        }
    }
}
