package com.aier.ardemo.network.api;


import com.aier.ardemo.bean.ArListBean;
import com.aier.ardemo.bean.CommonResult;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.bean.VersionResult;
import com.aier.ardemo.network.response.Response;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CommonApi {

    @POST("nkcleartext")
    Observable<Response<VersionResult>> getVerDataForBody(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<ResultBean>> getWenzhangDataForBody(@Body RequestBody body);//首页文章列表

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<ArListBean>> getArListData(@Body RequestBody body);//ar模型列表

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("nkcleartext")
    Observable<Response<CommonResult>> getDataForBody(@Body RequestBody body);//订单
}
