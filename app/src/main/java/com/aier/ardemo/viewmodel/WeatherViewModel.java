package com.aier.ardemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import com.aier.ardemo.http.datasource.WeatherDataSource;
import com.aier.ardemo.http.repo.WeatherRepo;
import com.aier.ardemo.model.Weather;
import com.aier.ardemo.viewmodel.base.BaseViewModel;

import okhttp3.ResponseBody;


public class WeatherViewModel extends BaseViewModel {

    private MutableLiveData<ResponseBody> weatherLiveData;
    private WeatherRepo weatherRepo;

    public WeatherViewModel() {
        weatherLiveData = new MutableLiveData<>();
        weatherRepo = new WeatherRepo(new WeatherDataSource(this));
    }

    public void createWeatherCode(String text) {
        weatherRepo.createWeather(text).observe(lifecycleOwner, weather -> weatherLiveData.setValue(weather));
    }

    public MutableLiveData<ResponseBody> getWeatherLiveData() {
        return weatherLiveData;
    }
}
