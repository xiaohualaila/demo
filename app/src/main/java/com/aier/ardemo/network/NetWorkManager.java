package com.aier.ardemo.network;

import com.aier.ardemo.network.request.Request;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zaifeng on 2018/2/28.
 * API初始化类
 */

public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile Request request = null;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Request.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              //  .client(client)
                .build();
    }

    public static Request getRequest() {
        if (request == null) {
            synchronized (Request.class) {
                request = retrofit.create(Request.class);
            }
        }
        return request;
    }


}