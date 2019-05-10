package com.aier.ardemo.ui.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.dialog.DialogFragment;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.fragment.HomeFragment;
import com.aier.ardemo.ui.fragment.MyFragment;
import com.aier.ardemo.ui.fragment.WeatherFragment;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.BottomView;
import com.baidu.ar.bean.DuMixARConfig;
import com.google.gson.Gson;
import com.karics.library.zxing.android.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements BottomView.BottomCallBack{
    @BindView(R.id.bottom_view)
    BottomView bottomView;
    private static String TAG ="MainActivity";
    /**
     * 需要手动申请的权限
     */
    private static final String[] ALL_PERMISSIONS = new String[] {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO};
    private Fragment mCurrentFrag;
    private FragmentManager fm;
 //   private Fragment homeFragment;
    private Fragment weatherFragment;
    private Fragment myFragment;

    @Override
    protected void initViews() {
        fm = getSupportFragmentManager();
        weatherFragment = new WeatherFragment();
        myFragment = new MyFragment();
        switchContent(weatherFragment);
        bottomView.setBottomCallBack(this);
        requestPermission();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Log.i("sss"," screenWidth"+screenWidth);
        Log.i("sss"," screenHeight"+screenHeight);
    }

    private void requestPermission() {
        // 6.0以下版本直接同意使用权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasNecessaryPermission()) {
                ActivityCompat.requestPermissions(this,ALL_PERMISSIONS, 1123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    Log.i(TAG,"权限申请成功！");
                } else {
                    // permission denied, boo! Disable the
                    Log.i(TAG,"权限申请失败！");
                    showMissingPermissionDialog();
                }
                return;
            }
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
    protected void initDate(Bundle savedInstanceState) {
        DuMixARConfig.setAppId("16021623"); // 设置App Id
        DuMixARConfig.setAPIKey("ZI0SDxDIWvtMnHvs2scKXC2x"); // 设置API Key
        DuMixARConfig.setSecretKey("ncNvjMB2QpFm6eaU9UGjkNxnk4oPxlIk");    // 设置Secret Key
        String userData = SharedPreferencesUtil.getString(this, "usersData", "usersData", "");
        if (!TextUtils.isEmpty(userData)) {
            Gson gson = new Gson();
            Person userDatas = gson.fromJson(userData, Person.class);
            GloData.setPersons(userDatas);
        }else {
            Person person =new Person();
            person.setUsername("");
            GloData.setPersons(person);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:

                DialogFragment dialogFragment = DialogFragment.getInstance();
                dialogFragment.show(getSupportFragmentManager(), () -> dialogFragment.dismiss());
                break;
        }

    }


    public void goToArActivity(){
        Intent intent = new Intent(this, ARActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ar_key", "10301883");
        bundle.putInt("ar_type", 5);
        bundle.putString("ar_path", "");
        bundle.putString("name", "白色椅子");
        bundle.putString("description", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToVRVideoActivity(){
        Intent intent = new Intent(this, VRActivity.class);
        startActivity(intent);
    }


    @Override
    public void setCallBack(int num) {
        switch (num){
            case 1:
                switchContent(weatherFragment);
                break;
            case 2:
                switchContent(myFragment);
                break;
        }
    }

    /**
     * 动态添加fragment，不会重复创建fragment
     *
     * @param to 将要加载的fragment
     */
    public void switchContent(Fragment to) {
        if (mCurrentFrag != to) {
            if (!to.isAdded()) {// 如果to fragment没有被add则增加一个fragment
                if (mCurrentFrag != null) {
                    fm.beginTransaction().hide(mCurrentFrag).commit();
                }
                fm.beginTransaction()
                        .add(R.id.fl_content, to)
                        .commit();
            } else {
                fm.beginTransaction().hide(mCurrentFrag).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFrag = to;
        }
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


}