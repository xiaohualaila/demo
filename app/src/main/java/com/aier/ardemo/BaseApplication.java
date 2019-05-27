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
        disableAPIDialog();
    }

    public static Application getAppContext() {
        return appContext;
    }

    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog(){
        if (Build.VERSION.SDK_INT < 28)return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
