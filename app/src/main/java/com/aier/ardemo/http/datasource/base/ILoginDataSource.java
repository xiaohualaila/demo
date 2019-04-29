package com.aier.ardemo.http.datasource.base;


import com.aier.ardemo.http.basis.callback.RequestCallback;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.QrCode;

/**
 * 作者：leavesC
 * 时间：2018/10/27 21:10
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public interface ILoginDataSource {

    void createOrder(String text, int width, RequestCallback<Order> callback);

}
