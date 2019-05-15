package com.aier.ardemo;

import android.app.Application;

import com.aier.ardemo.utils.NetUtils;


public class BaseApplication extends Application {

    private static Application appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        NetUtils.setContext(this);
    }

    public static Application getAppContext() {
        return appContext;
    }
}
