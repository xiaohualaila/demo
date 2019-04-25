package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.view.View;
import com.aier.ardemo.R;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.weight.AddDeleteView;
import butterknife.BindView;
import butterknife.OnClick;


public class OrderInfoActivity extends BaseActivity implements AddDeleteView.OnAddDelClickListener{

    @BindView(R.id.add_dele)
    AddDeleteView adv;

    @Override
    protected void initViews() {
        adv.setOnAddDelClickListener(this);
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


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
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