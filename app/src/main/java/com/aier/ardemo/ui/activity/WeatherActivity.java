package com.aier.ardemo.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;
import com.aier.ardemo.R;
import com.aier.ardemo.model.Weather;
import com.aier.ardemo.ui.base.BaseActivity;
import com.aier.ardemo.viewmodel.WeatherViewModel;
import com.aier.ardemo.viewmodel.base.LViewModelProviders;
import com.google.gson.Gson;

import okhttp3.ResponseBody;


public class WeatherActivity extends BaseActivity {
    private String TAG = "xxx";

    private WeatherViewModel weatherViewModel;
    @Override
    protected void initViews() {

    }

    @Override
    protected void initDate(Bundle savedInstanceState) {
        weatherViewModel.createWeatherCode("杭州");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_weather;
    }

    @Override
    protected ViewModel initViewModel() {
        weatherViewModel = LViewModelProviders.of(this, WeatherViewModel.class);
        weatherViewModel.getWeatherLiveData().observe(this, this::handleQrCode);
        return weatherViewModel;
    }

    private void handleQrCode(ResponseBody order) {



        Log.i(TAG,new Gson().toJson(order));
    }







}
