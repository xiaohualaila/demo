package com.aier.ardemo.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.fragment.FirstFragment;
import com.aier.ardemo.ui.fragment.MyFragment;
import com.aier.ardemo.utils.CheckAppInstalledUtil;
import com.aier.ardemo.utils.NetUtil;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.BottomView;
import com.baidu.ar.bean.DuMixARConfig;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomView.BottomCallBack {
    @BindView(R.id.bottom_view)
    BottomView bottomView;
    private static String TAG ="MainActivity";

    private Fragment mCurrentFrag;
    private FragmentManager fm;
    private Fragment firstFragment;
    private Fragment myFragment;

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
        fm = getSupportFragmentManager();
        firstFragment = new FirstFragment();
        myFragment = new MyFragment();
        switchContent(firstFragment);
        bottomView.setBottomCallBack(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Log.i("sss"," screenWidth"+screenWidth);
        Log.i("sss"," screenHeight"+screenHeight);
    }

    @Override
    protected void initDate() {
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
            person.setId("111");
            person.setUsername("小虎");
            person.setAddress("杭州");
            person.setAddress("男");
//            person.setHeadimg("person");
            GloData.setPersons(person);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    public void goToArActivity(){
        Intent intent = new Intent(this, ARActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ar_key", "10302537");
        bundle.putInt("ar_type", 5);
        bundle.putString("ar_path", "");
        bundle.putString("name", "白色椅子");
        bundle.putString("description", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToVRVideoActivity(){
       boolean isInStall = CheckAppInstalledUtil.isInstalled(mContext,"com.panoeye.peplayer");
         if(isInStall){
             Intent intent = new Intent(Intent.ACTION_MAIN);
             /**知道要跳转应用的包命与目标Activity*/
             ComponentName componentName = new ComponentName("com.panoeye.peplayer", "com.panoeye.peplayer.OnlineUserSettingActivity");
             intent.setComponent(componentName);
             intent.putExtra("", "");//这里Intent传值
             startActivity(intent);
             Log.i("sss","  已下载");
         }else {
             Log.i("sss","  未下载");
             toastLong("未下载VR app");
         }

    }


    public void goToWebActivity(String url,String title){
        WebActivity.startToWebAc(this,title,url,3);
    }

    @Override
    public void setTabCallBack(int num) {
        switch (num){
            case 1:
                switchContent(firstFragment);
                break;
            case 2:
                switchContent(myFragment);
                break;
            case 3:
             //   switchContent(arListFragment);
                if(NetUtil.isConnected(mContext)){
                    startActiviys(YubaiActivity.class);
                }else {
                    toastLong("网路无法连接请检查网路！");
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
               // toastLong("没有扫描到结果！");
            } else {
                WebActivity.startToWebAc(this,"南康智能家具产业联盟防伪系统",result.getContents(),1);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}