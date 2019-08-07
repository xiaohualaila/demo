package com.aier.ardemo.ui.model;


import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.ui.contract.ArContract;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class ArModel implements ArContract.Model {


    @Override
    public Observable<ResponseBody> getArListData(RequestBody body) {
        return NetWorkManager.getRequest().getArListData(body);
    }
}
