package com.aier.ardemo;

import android.app.Application;


public class BaseApplication extends Application {

    private static Application appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

    }

    public static Application getAppContext() {
        return appContext;
    }
}
