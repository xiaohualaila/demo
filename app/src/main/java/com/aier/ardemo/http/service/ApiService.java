package com.aier.ardemo.http.service;



import com.aier.ardemo.http.basis.model.BaseResponseBody;
import com.aier.ardemo.model.Weather;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {






    @GET("weather_mini")
    Observable<BaseResponseBody<ResponseBody>> queryWeather(@Query("city") String cityName);

}
