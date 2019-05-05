package com.aier.ardemo.http.datasource.base;

import com.aier.ardemo.http.basis.callback.RequestCallback;
import okhttp3.ResponseBody;


public interface IWeatherDataSource {

    void createWeather(String text, RequestCallback<ResponseBody> callback);
}
