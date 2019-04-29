package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.aier.ardemo.R;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.Weather;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.viewmodel.OrderViewModel;
import com.aier.ardemo.viewmodel.base.LViewModelProviders;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    private OrderViewModel orderViewModel;
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

    @Override
    protected ViewModel initViewModel() {
        orderViewModel = LViewModelProviders.of(this, OrderViewModel.class);
        orderViewModel.getOrderLiveData().observe(this, this::handleQrCode);
        return orderViewModel;
    }

    private void handleQrCode(Weather order) {
        StringBuilder result = new StringBuilder();
        for (Weather.InnerWeather.NearestWeather nearestWeather : order.getData().getWeather()) {
            result.append("\n\n").append(new Gson().toJson(nearestWeather));
        }

        Log.i("sss",result.toString());
    }

    public void requestServer(View view) {

        orderViewModel.createOrderCode("杭州");
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
