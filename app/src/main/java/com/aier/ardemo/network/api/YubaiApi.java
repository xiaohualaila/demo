package com.aier.ardemo.network.api;



import com.aier.ardemo.bean.YUBAIBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface YubaiApi {

    @GET("query")
    Observable<YUBAIBean> getYUBAIData(@Query("type") String type, @Query("data") String data);


}
