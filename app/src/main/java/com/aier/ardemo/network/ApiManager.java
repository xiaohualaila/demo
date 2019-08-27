package com.aier.ardemo.network;


import com.aier.ardemo.network.api.CommonApi;
import com.aier.ardemo.network.api.YubaiApi;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {

    private CommonApi commonApi;
    private YubaiApi yubaiApi;


    private static ApiManager sApiManager;

    private static OkHttpClient mClient;

    private ApiManager() {

    }

    public static ApiManager getInstence() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        mClient = new OkHttpClient.Builder()
                .addInterceptor(new CustomInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        return sApiManager;
    }


    /**
     * 封装 羽白 API
     */
    public YubaiApi getYubaiService() {
        if (yubaiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.YUBAI_API_URL)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            yubaiApi = retrofit.create(YubaiApi.class);
        }
        return yubaiApi;
    }


    /**
     * 封装 通用API
     */
    public CommonApi getCommonService() {
        if (commonApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASIC_API_URL)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            commonApi = retrofit.create(CommonApi.class);
        }
        return commonApi;
    }


}
