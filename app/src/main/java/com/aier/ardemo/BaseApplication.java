package com.aier.ardemo;

import android.app.Application;
import com.aier.ardemo.network.NetWorkManager;
import com.raizlabs.android.dbflow.config.FlowManager;

public class BaseApplication extends Application {

    private static Application appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        NetWorkManager.getInstance().init();
        //初始化DBFLOW
        FlowManager.init(this);
    }

    public static Application getAppContext() {
        return appContext;
    }

}
