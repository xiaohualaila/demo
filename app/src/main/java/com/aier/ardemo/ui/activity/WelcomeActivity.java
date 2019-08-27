package com.aier.ardemo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.aier.ardemo.BuildConfig;
import com.aier.ardemo.R;
import com.aier.ardemo.dialog.Apk_dialog;
import com.aier.ardemo.dialog.DialogFragment;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.contract.WelContract;
import com.aier.ardemo.ui.presenter.WelPresenter;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.utils.StatusBarUtil;
import com.aier.ardemo.weight.AppDownload;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity implements AppDownload.Callback, WelContract.View {
    private static String TAG ="WelcomeActivity";
    private static final String[] ALL_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};

    Apk_dialog apk_dialog;
    SeekBar seek;
    TextView numProBar;
    String path;

    private WelPresenter presenter;
    @Override
    protected void beforeInit() {
        super.beforeInit();
        StatusBarUtil.INSTANCE.setTranslucent(this);
    }
    @Override
    protected void initDate() {
        presenter = new WelPresenter(this);
        Timer time = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                requestPermission();
            }
        };time.schedule(tk, 500);
    }

    @Override
    protected void initViews() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        Log.i("sss","width  "+ width);
        Log.i("sss","height  "+ height);
        Log.i("sss","density  "+ density);
        Log.i("sss","densityDpi  "+ densityDpi);
        //  drawable-ldpi        120DPI
        //  drawable-mdpi        160DPI
        //  drawable-hdpi        240DPI
        //  drawable-xhdpi       320DPI
        //  drawalbe-xxhdpi      480DPI
        //  drawable-xxxhdpi     640DPI

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
            }else {
                presenter.checkAppVersion();
            }
        }else {
                presenter.checkAppVersion();
        }
    }

    private boolean hasNecessaryPermission() {
        List<String> permissionsList = new ArrayList();
            for (String permission : ALL_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
        }
        return permissionsList.size() == 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG,"权限申请成功！");
                       presenter.checkAppVersion();
                } else {
                    Log.i(TAG,"权限申请失败！");
                    showMissingPermissionDialog();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10086:
                installProcess();
                return;
        }
    }

    private void updataApp(String url) {
        path = Environment.getExternalStorageDirectory()+"/nkjj/" + "南康家居.apk" ;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        apk_dialog = new Apk_dialog( WelcomeActivity.this );
        if (apk_dialog != null && apk_dialog.isShowing()) {
            return;
        }

        AppDownload appDownload = new AppDownload();
        appDownload.setProgressInterface(WelcomeActivity.this);
        appDownload.downApk(url, WelcomeActivity.this);
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

    @Override
    public void callProgress(int progress) {
        if (progress >= 100) {
            runOnUiThread(() -> {
                apk_dialog.dismiss();
                installProcess();
            });

        }else {
            runOnUiThread(() -> {
                seek.setProgress( progress );
                numProBar.setText(progress+"%");
            });
        }
    }

    //安装应用的流程
    private void installProcess() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                DialogFragment.getInstance().show(getSupportFragmentManager(), () -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startInstallPermissionSettingActivity();
                    }
                });
                return;
            }
        }
        //有权限，开始安装应用程序
        installApk();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 10086);
    }

    /**
     * 开启安装过程
     */
    private void installApk() {
        File file = new File(path);
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

    @Override
    public void updateVer(String url) {
        updataApp(url);
    }

    @Override
    public void toActivity() {
        isShowGuidance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}
