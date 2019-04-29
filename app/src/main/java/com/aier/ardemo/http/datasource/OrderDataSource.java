package com.aier.ardemo.http.datasource;


import com.aier.ardemo.http.basis.BaseRemoteDataSource;
import com.aier.ardemo.http.basis.callback.RequestCallback;
import com.aier.ardemo.http.basis.config.HttpConfig;
import com.aier.ardemo.http.datasource.base.IOrderDataSource;
import com.aier.ardemo.http.service.ApiService;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.Weather;
import com.aier.ardemo.viewmodel.base.BaseViewModel;

/**
 * 作者：leavesC
 * 时间：2018/10/27 20:48
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class OrderDataSource extends BaseRemoteDataSource implements IOrderDataSource {

    public OrderDataSource(BaseViewModel baseViewModel) {
        super(baseViewModel);
    }

    @Override
    public void createOrder(String text, RequestCallback<Weather> callback) {
        execute(getService(ApiService.class).queryWeather(text), callback);
    }

}