/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.aier.ardemo.ui.fragment.ARFragment;
import com.aier.ardemo.R;
import com.aier.ardemo.utils.AndroidWorkaround;
import com.aier.ardemo.utils.StatusBarUtil;

public class ARActivity extends FragmentActivity {

    private ARFragment mARFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.INSTANCE.setTranslucent(this);
        setContentView(R.layout.activity_ar);

        if (findViewById(R.id.bdar_id_fragment_container) != null) {
            FragmentManager  fragmentManager = getSupportFragmentManager();
            FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();

            mARFragment = new ARFragment();
            mARFragment.setArguments(getIntent().getExtras());
            // 将trackArFragment设置到布局上
            fragmentTransaction.replace(R.id.bdar_id_fragment_container, mARFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        // 底部导航栏适配
        if (AndroidWorkaround.Companion.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.Companion.assistActivity(findViewById(android.R.id.content));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mARFragment.onFragmentBackPressed();
    }

}