package com.aier.ardemo.netsubscribe;

import com.aier.ardemo.model.BaseRequestBean;
import com.aier.ardemo.model.WeatherRequestBean;
import com.aier.ardemo.netutils.RetrofitFactory;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Created by 眼神 on 2018/3/27.
 * 建议：把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe、天气类WeatherSubscribe
 */

public class CheckAPPVersionSubscribe {

    /**
     * 获取天气数据@Query
     */
    public static void getAppVer( DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getAppVer();
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

}
