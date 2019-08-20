package com.aier.ardemo.ui.model;

import com.aier.ardemo.bean.CommonResult;
import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.OrderContract;
import io.reactivex.Observable;
import okhttp3.RequestBody;


public class OrderModel implements OrderContract.Model {


    @Override
    public Observable<Response<CommonResult>> updateOrder(RequestBody body) {
        return  NetWorkManager.getRequest().getDataForBody(body);
    }
}
