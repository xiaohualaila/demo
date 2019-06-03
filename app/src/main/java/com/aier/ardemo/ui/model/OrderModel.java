package com.aier.ardemo.ui.model;

import com.aier.ardemo.bean.OrderResultBean;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.FirstContract;
import com.aier.ardemo.ui.contract.OrderContract;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class OrderModel implements OrderContract.Model {


    @Override
    public Observable<Response<OrderResultBean>> updateOrder(RequestBody body) {
        return  NetWorkManager.getRequest().getDataForBody(body);
    }
}
