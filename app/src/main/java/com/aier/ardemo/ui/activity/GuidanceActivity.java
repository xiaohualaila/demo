package com.aier.ardemo.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.SamplePagerAdapter;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.arview.CircleIndicator;
import com.aier.ardemo.utils.StatusBarUtil;

import butterknife.BindView;

public class GuidanceActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    protected void beforeInit(){
        StatusBarUtil.INSTANCE.setTranslucent(this);
    }
    @Override
    protected void initViews() {
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
