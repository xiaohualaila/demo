package com.aier.ardemo.ui.model;

import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.FirstContract;
import io.reactivex.Observable;
import okhttp3.RequestBody;


public class FirstModel implements FirstContract.Model {


    @Override
    public Observable<Response<ResultBean>> getWenzhangData(RequestBody body) {
        return NetWorkManager.getRequest().getWenzhangDataForBody(body);
    }
}
