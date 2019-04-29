package com.aier.ardemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.aier.ardemo.http.datasource.OrderDataSource;
import com.aier.ardemo.http.repo.OrderRepo;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.QrCode;
import com.aier.ardemo.model.Weather;
import com.aier.ardemo.viewmodel.base.BaseViewModel;
import android.arch.lifecycle.Observer;

/**
 * 作者：leavesC
 * 时间：2018/10/27 21:14
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class OrderViewModel extends BaseViewModel {

    private MutableLiveData<Weather> orderLiveData;

    private OrderRepo orderRepo;

    public OrderViewModel() {
        orderLiveData = new MutableLiveData<>();
        orderRepo = new OrderRepo(new OrderDataSource(this));
    }

    public void createOrderCode(String text) {
        orderRepo.createOrder(text).observe(lifecycleOwner, order -> orderLiveData.setValue(order));
    }

    public MutableLiveData<Weather> getOrderLiveData() {
        return orderLiveData;
    }

}
