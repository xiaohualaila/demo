package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.view.View;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import butterknife.OnClick;


public class OrderActivity extends BaseActivity{

    @Override
    protected void initViews() {

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order;
    }

    @Override
    protected ViewModel initViewModel() {

        return null;
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
        }

    }





}