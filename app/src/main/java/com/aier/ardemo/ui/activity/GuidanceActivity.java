package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.SamplePagerAdapter;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.view.CircleIndicator;
import butterknife.BindView;

public class GuidanceActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;


    @Override
    protected void initViews() {

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        viewpager.setAdapter(new SamplePagerAdapter());
        indicator.setViewPager(viewpager);

        SamplePagerAdapter.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    startActiviys(MainActivity.class);
                    finish();
                }
            }
        };
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_guidance;
    }

}
