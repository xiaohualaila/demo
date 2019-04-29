package com.aier.ardemo.http.datasource.base;

import com.aier.ardemo.http.basis.callback.RequestCallback;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.Weather;


public interface IOrderDataSource {

    void createOrder(String text, RequestCallback<Weather> callback);

}
