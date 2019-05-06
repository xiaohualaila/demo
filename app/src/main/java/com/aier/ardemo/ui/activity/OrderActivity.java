package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void initViews() {
        tv_title.setText("我的订单");
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order;
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
