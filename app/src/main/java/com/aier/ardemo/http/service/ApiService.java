package com.aier.ardemo.http.service;


import com.aier.ardemo.http.basis.config.HttpConfig;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 作者：leavesC
 * 时间：2018/10/28 13:13
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public interface ApiService {



    @Headers({HttpConfig.HTTP_REQUEST_TYPE_KEY + ":" + HttpConfig.HTTP_REQUEST_QR_CODE})
    @GET("qrcode/api")
    Observable createQrCode(@Query("text") String text, @Query("w") int width);


}
