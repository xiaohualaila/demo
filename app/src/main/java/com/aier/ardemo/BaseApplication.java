package com.aier.ardemo;

import android.app.Application;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BaseApplication extends Application {

    private static Application appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        //初始化DBFLOW
        FlowManager.init(this);
    }

    public static Application getAppContext() {
        return appContext;
    }

}
