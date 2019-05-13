package com.aier.ardemo.netsubscribe;

import com.aier.ardemo.netutils.RetrofitFactory;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by 眼神 on 2018/3/27.
 * 建议：把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe、天气类WeatherSubscribe
 */

public class CheckAPPVersionSubscribe {

    /**
     * 获取天气数据@Query
     */
    public static void getAppVer(RequestBody body,DisposableObserver<ResponseBody> subscriber) {
//        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getAppVer();
//        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);



        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getAppVerForBody(body);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

}
