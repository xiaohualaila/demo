package com.aier.ardemo.ui.model;


import com.aier.ardemo.bean.ArListBean;
import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.ui.contract.ArContract;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import com.aier.ardemo.network.response.Response;

public class ArModel implements ArContract.Model {


    @Override
    public Observable<Response<ArListBean>> getArListData(RequestBody body) {
        return NetWorkManager.getRequest().getArListData(body);
    }
}
