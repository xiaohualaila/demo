package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.weight.AddDeleteView;
import butterknife.BindView;
import butterknife.OnClick;


public class OrderInfoActivity extends BaseActivity implements AddDeleteView.OnAddDelClickListener{

    @BindView(R.id.add_dele)
    AddDeleteView adv;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void initViews() {
        adv.setOnAddDelClickListener(this);
        tv_title.setText("订单信息");
    }

    @Override
    protected void initDate(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order_info;
    }

    @Override
    protected ViewModel initViewModel() {

        return null;
    }


    @OnClick({R.id.iv_back,R.id.rl_order_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_order_info:
                startActivity(new Intent(this,OrderActivity.class));
                break;
        }

    }


    @Override
    public void onAddClick(View v) {
    int origin = adv.getNumber();
    origin++; adv.setNumber(origin);

    }

    @Override
    public void onDelClick(View v) {
        int origin = adv.getNumber();
        origin--;
        adv.setNumber(origin);
    }
}