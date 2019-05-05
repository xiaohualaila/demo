package com.aier.ardemo.http.repo;

import android.arch.lifecycle.MutableLiveData;
import com.aier.ardemo.http.basis.BaseRepo;
import com.aier.ardemo.http.datasource.base.IWeatherDataSource;
import com.aier.ardemo.model.Weather;

import okhttp3.ResponseBody;


public class WeatherRepo extends BaseRepo<IWeatherDataSource> {

    public WeatherRepo(IWeatherDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<ResponseBody> createWeather(String text) {
        MutableLiveData<ResponseBody> liveData = new MutableLiveData<>();
        remoteDataSource.createWeather(text, data -> {
            liveData.setValue(data);
        });
        return liveData;
    }


}