package com.aier.ardemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SetUpActivity  extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        tv_title.setText("设置");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setup;
    }

    @OnClick({R.id.iv_back,R.id.person_info,R.id.address,R.id.version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.person_info:
                startActivity(new Intent(this, PersonInfoActivity.class));
                finish();
                break;
            case R.id.address:
                startActivity(new Intent(this, AddressActivity.class));
                finish();
                break;
            case R.id.version:
                toastLong("当前版本已是最新版本");
                break;
        }
    }
}
