package com.aier.ardemo.network.request;

import com.aier.ardemo.bean.ArListBean;
import com.aier.ardemo.bean.OrderResultBean;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.response.Response;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by Zaifeng on 2018/2/28.
 * 封装请求的接口
 */

public interface Request {

    String HOST = "https://cloud.zq12369.com/nodeapi/";
   // String HOST = "https://192.168.0.5:3000/nodeapi/";
    String YUYIN_URL = "https://api.zq-ai.com/zqcloudapi/v1.0/";//羽白语音

    @GET("query")
    Call<YUBAIBean> getWeatherData(@Query("type") String type, @Query("data") String data);


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<ResultBean>> getWenzhangDataForBody(@Body RequestBody body);//首页文章列表

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<OrderResultBean>> getDataForBody(@Body RequestBody body);//首页文章列表


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<ArListBean>> getArListData(@Body RequestBody body);//ar模型列表
}
