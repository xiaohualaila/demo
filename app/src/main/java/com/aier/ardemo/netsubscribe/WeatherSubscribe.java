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

public class WeatherSubscribe {

    /**
     * 获取天气数据@Query
     */
    public static void getWeatherDataForQuery(String cityName, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getWeatherDataForQuery("v1",cityName);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取天气数据@QueryMap
     */
    public static void getWeatherDataForMap(String cityName, DisposableObserver<ResponseBody> subscriber) {
        Map<String, String> map = new HashMap<>();
        map.put("version","v1");
        map.put("city",cityName);
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getWeatherDataForMap(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取天气数据@Body
     */
    public static void getWeatherDataForBody(String cityName, DisposableObserver<ResponseBody> subscriber) {
        WeatherRequestBean bean = new WeatherRequestBean();
        bean.setCity(cityName);
        BaseRequestBean<WeatherRequestBean> requestBean = new BaseRequestBean<>();
        requestBean.setObj(bean);
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getWeatherDataForBody(requestBean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
