/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.aier.ardemo.dialog.DialogSelectFragment;
import com.aier.ardemo.ui.fragment.ARFragment;
import com.aier.ardemo.R;

public class ARActivity extends FragmentActivity {

    private ARFragment mARFragment;

    private DialogSelectFragment dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    }

    private int type = 1;
    private int age = 1;
    public void showDialog(){
        dialog = DialogSelectFragment.getInstance(type,age);
        dialog.show(getSupportFragmentManager(), new DialogSelectFragment.OnDialogButtonClickListener() {
            @Override
            public void buttonClick(int type, int age) {
                type = type;
                age = age;
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mARFragment.onFragmentBackPressed();
    }

}