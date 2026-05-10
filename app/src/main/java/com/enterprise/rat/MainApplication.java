package com.enterprise.rat;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class MainApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return appContext;
    }
}
