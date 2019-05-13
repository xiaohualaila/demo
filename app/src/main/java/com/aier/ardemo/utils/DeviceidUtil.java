package com.aier.ardemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * author : Rain
 * time : 2017/10/17 0017
 * explain :
 */

public class DeviceidUtil {


    /**
     * 获取手机ID
     *
     * @param context
     * @return
     */
    public static String deviceid(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        return id;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            int versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
