package com.aier.ardemo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aier.ardemo.BuildConfig;
import com.aier.ardemo.R;
import com.aier.ardemo.dialog.Apk_dialog;
import com.aier.ardemo.model.WeatherResponseBean;
import com.aier.ardemo.netsubscribe.CheckAPPVersionSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.DeviceidUtil;
import com.aier.ardemo.utils.GsonUtils;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.AppDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity implements AppDownload.Callback{
    private static String TAG ="WelcomeActivity";
    private static final String[] ALL_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    private static Handler handler = new Handler();
    Apk_dialog apk_dialog;
    SeekBar seek;
    TextView numProBar;
    String path;
    @Override
    protected void initDate(Bundle savedInstanceState) {
//
//        Timer time = new Timer();
//        TimerTask tk = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };time.schedule(tk, 500);
        requestPermission();
    }

    @Override
    protected void initViews() {
    }


    private void isShowGuidance(){
        boolean  isOne = SharedPreferencesUtil.getBoolean(WelcomeActivity.this, "isOne", true);
        if(isOne){
            startActiviys(GuidanceActivity.class);
        }else {
            startActiviys(MainActivity.class);
        }
        SharedPreferencesUtil.putBoolean(WelcomeActivity.this, "isOne", false);
        finish();
    }



    private void requestPermission() {
        // 6.0以下版本直接同意使用权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasNecessaryPermission()) {
                ActivityCompat.requestPermissions(this,ALL_PERMISSIONS, 1123);
            }
        }else {
            checkAppVersion();
        }
    }



    private boolean hasNecessaryPermission() {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList.size() == 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG,"权限申请成功！");
                    checkAppVersion();
                } else {
                    Log.i(TAG,"权限申请失败！");
                    showMissingPermissionDialog();
                }
                return;
            }
        }
    }

    private void checkAppVersion() {
//        CheckAPPVersionSubscribe.getAppVer(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
//            @Override
//            public void onSuccess(String result) {
//               // WeatherResponseBean weather = GsonUtils.fromJson(result, WeatherResponseBean.class);
//
//              //  Log.i("sss","sss"+weather.toString());
//              //  showWeatherText(weather);
//                 String versionName = DeviceidUtil.getAppVersionName(WelcomeActivity.this);
//                 SharedPreferencesUtil.putString(WelcomeActivity.this,"app_ver",versionName);
//                 updataApp("");
//            }
//
//            @Override
//            public void onFault(String errorMsg) {
//                //失败
//               // Toast.makeText(getActivity(), "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
//                todo();
//            }
//        }));
        todo();
    }

    private void updataApp(String url) {
         path = Environment.getExternalStorageDirectory()+"/zhsq/" + "南康家居防伪.apk" ;
        apk_dialog = new Apk_dialog( WelcomeActivity.this );
        if (apk_dialog != null && apk_dialog.isShowing()) {
            return;
        }
        File file = new File(path);
        if(file != null){
            file.delete();
        }
        AppDownload appDownload = new AppDownload();
        appDownload.setProgressInterface(WelcomeActivity.this);
        appDownload.downApk(url,WelcomeActivity.this);
        apk_dialog.show();
        apk_dialog.setCancelable( false );
        seek = apk_dialog.getSeekBar();
        numProBar= apk_dialog.getNumProBar();

    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton("取消", (dialog, which) -> finish());
        builder.setPositiveButton("设置", (dialog, which) -> startAppSettings());
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_wel;
    }

    private void todo(){
        handler.postDelayed(runnable,500);
    }

    private Runnable runnable = () -> isShowGuidance();


    @Override
    public void callProgress(int progress) {
        if (progress >= 100) {
            runOnUiThread(() -> {
                apk_dialog.dismiss();

                install(path);
            });

        }else {
            runOnUiThread(() -> {
                seek.setProgress( progress );
                numProBar.setText(progress+"%");
            });
        }
    }

    /**
     * 开启安装过程
     * @param fileName
     */
    private void install(String fileName) {

        File file = new File(fileName);
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_VIEW );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        //判读版本是否在7.0以上
        Uri apkUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            apkUri = FileProvider.getUriForFile( this, BuildConfig.APPLICATION_ID + ".fileprovider", file );
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);
        }
        intent.setDataAndType( apkUri, "application/vnd.android.package-archive" );
        startActivity( intent );
    }
}
