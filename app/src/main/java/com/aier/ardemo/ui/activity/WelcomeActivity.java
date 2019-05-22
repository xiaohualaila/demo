package com.aier.ardemo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import com.aier.ardemo.BuildConfig;
import com.aier.ardemo.R;
import com.aier.ardemo.dialog.Apk_dialog;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.netsubscribe.CheckAPPVersionSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.DESUtil;
import com.aier.ardemo.utils.DesUtils;
import com.aier.ardemo.utils.DeviceidUtil;
import com.aier.ardemo.utils.Md5Util;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.AppDownload;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO};

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
               // isShowGuidance();
                checkAppVersion2();
            }
        }else {
              // isShowGuidance();
                checkAppVersion2();
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
                    // isShowGuidance();
                       checkAppVersion2();
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
            JSONObject obj =new JSONObject();
            JSONObject object1 =new JSONObject();

            Long timestamp = new Date().getTime();
            obj.put("appId", URLConstant.APPID);
            obj.put("method","NKCLOUDAPI_GETLASTAPP");
            obj.put("timestamp",timestamp);
            obj.put("clienttype","WEB");
            obj.put("object",object1);
            String md5_str = Md5Util.getMd5(URLConstant.APPID+"NKCLOUDAPI_GETLASTAPP"+ timestamp +"WEB" +object1.toString());
            obj.put("secret",md5_str);
            Log.i(TAG,obj.toString());

            String desStr = DESUtil.encrypt(obj.toString());//DES加密
            Log.i(TAG,desStr);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),desStr);
//            CheckAPPVersionSubscribe.getAppVer(body,new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i(TAG,"  result" + result);
//                   // WeatherResponseBean weather = GsonUtils.fromJson(result, WeatherResponseBean.class);
//
//                  //  Log.i("sss","sss"+weather.toString());
//                  //  showWeatherText(weather);
//                     String versionName = DeviceidUtil.getAppVersionName(WelcomeActivity.this);
//                     SharedPreferencesUtil.putString(WelcomeActivity.this,"app_ver",versionName);
//                     updataApp("");
//                }
//
//                @Override
//                public void onFault(String errorMsg) {
//                    Log.i(TAG,"  errorMsg" + errorMsg);
//                    showToast(errorMsg);
//                }
//            }));

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.BASE_URL_LOCAL)
                    .build();
            HttpApi service = retrofit.create(HttpApi.class);
            Call<ResponseBody> call = service.getAppVerForBody2(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 已经转换为想要的类型了
                    try {
                        String str = response.body().string();

                        Log.i(TAG,"返回数据 " + str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Log.i(TAG,"  errorMsg" + t.getMessage());
                }


            });

        }catch (Exception e){
            e.getMessage();
        }


    }

    private void checkAppVersion2() {
        try {
            JSONObject obj =new JSONObject();
            JSONObject obj1 =new JSONObject();
            obj.put("method","NKCLOUDAPI_GETLASTAPP");
            obj.put("params",obj1);

            Log.i(TAG,obj.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.BASE_URL_LOCAL)
                    .build();
            HttpApi service = retrofit.create(HttpApi.class);
            Call<ResponseBody> call = service.getAppVerForBody2(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 已经转换为想要的类型了
                    try {
                        String str = response.body().string();
                  //      Log.i(TAG,"返回数据 " + str);
                        isShowGuidance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Log.i(TAG,"  errorMsg" + t.getMessage());
                }
            });

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
