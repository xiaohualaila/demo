package com.aier.ardemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;

    @Override
    protected void initViews() {
        tv_title.setText("支付结果");
        tv_right.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay;
    }



    @OnClick({R.id.iv_back,R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                finish();
                break;
        }
    }
}
