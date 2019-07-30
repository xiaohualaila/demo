package com.aier.ardemo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import com.aier.ardemo.BuildConfig;
import com.aier.ardemo.R;
import com.aier.ardemo.dialog.Apk_dialog;
import com.aier.ardemo.network.URLConstant;
import com.aier.ardemo.network.request.Request;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.AppDownload;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomeActivity extends BaseActivity implements AppDownload.Callback{
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
    @Override
    protected void initDate(Bundle savedInstanceState) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明状态栏
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
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
                isShowGuidance();
                //checkAppVersion();
            }
        }else {
               isShowGuidance();
              //  checkAppVersion();
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
            case 1123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG,"权限申请成功！");
                     isShowGuidance();
                     //  checkAppVersion();
                } else {
                    Log.i(TAG,"权限申请失败！");
                    showMissingPermissionDialog();
                }
                return;
            }
        }
    }


    private void checkAppVersion() {
        try {
//            JSONObject obj =new JSONObject();
//            JSONObject obj1 =new JSONObject();
//            obj.put("method","NKCLOUDAPI_GETLASTAPP");
//            obj.put("params",obj1);
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
//            Retrofit retrofit = new Retrofit.Builder()
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl(URLConstant.BASE_URL_LOCAL)
//                    .build();
//            Request service = retrofit.create(Request.class);
//            Call<ResponseBody> call = service.getDataForBody(body);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    // 已经转换为想要的类型了
//                    try {
//                        String str = response.body().string();
//                        Log.i(TAG,"返回数据 " + str);
//                        isShowGuidance();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                    isShowGuidance();
//                    Log.i(TAG,"  errorMsg" + t.getMessage());
//                }
//            });

        }catch (Exception e){
            e.getMessage();
        }

    }


    private void updataApp(String url) {
         path = Environment.getExternalStorageDirectory()+"/nkjj/" + "南康家居防伪.apk" ;
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
