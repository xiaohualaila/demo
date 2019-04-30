package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.utils.SharedPreferencesUtil;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void initViews() {

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

        Timer time = new Timer();
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                boolean  isOne = SharedPreferencesUtil.getBoolean(WelcomeActivity.this, "isOne", true);
                if(isOne){
                    startActiviys(GuidanceActivity.class);
                }else {
                    startActiviys(MainActivity.class);
                }
                SharedPreferencesUtil.putBoolean(WelcomeActivity.this, "isOne", false);
                finish();
            }
        };time.schedule(tk, 500);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_wel;
    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }
}
