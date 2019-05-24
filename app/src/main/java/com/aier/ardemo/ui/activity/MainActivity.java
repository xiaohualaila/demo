package com.aier.ardemo.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.aier.ardemo.R;
import com.aier.ardemo.bean.GloData;
import com.aier.ardemo.bean.Person;
import com.aier.ardemo.dialog.DialogFragment;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.ui.fragment.MyFragment;
import com.aier.ardemo.ui.fragment.WeatherFragment;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import com.aier.ardemo.weight.BottomView;
import com.baidu.ar.bean.DuMixARConfig;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.google.gson.Gson;



import butterknife.BindView;



public class MainActivity extends BaseActivity implements BottomView.BottomCallBack {
    @BindView(R.id.bottom_view)
    BottomView bottomView;
    private static String TAG ="MainActivity";

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

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Log.i("sss"," screenWidth"+screenWidth);
        Log.i("sss"," screenHeight"+screenHeight);

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


//    @OnClick({R.id.fab})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.fab:
//
//                break;
//        }
//
//    }


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
//        Intent intent = new Intent(this, VRActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_MAIN);
      /**知道要跳转应用的包命与目标Activity*/
        ComponentName componentName = new ComponentName("com.panoeye.peplayer", "com.panoeye.peplayer.OnlineUserSettingActivity");
        intent.setComponent(componentName);
        intent.putExtra("", "");//这里Intent传值
        startActivity(intent);


    }


    public void goToWebActivity(String url,String title){
        Intent intent1 = new Intent(this, WebActivity.class);
        intent1.putExtra("codedContent", url);
        intent1.putExtra("title", title);
        intent1.putExtra("type", 3);
        startActivity(intent1);
    }

    @Override
    public void setTabCallBack(int num) {
        switch (num){
            case 1:
                switchContent(weatherFragment);
                break;
            case 2:
                switchContent(myFragment);
                break;
            case 3:
                startActiviys(BaiduVoiceActivity.class);

//                DialogFragment dialogFragment = DialogFragment.getInstance();
//                dialogFragment.show(getSupportFragmentManager(), () -> dialogFragment.dismiss());
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

}