package com.aier.ardemo.ui.model;

import com.aier.ardemo.bean.VersionResult;
import com.aier.ardemo.network.NetWorkManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.WelContract;
import io.reactivex.Observable;
import okhttp3.RequestBody;



public class WelcomeModel implements WelContract.Model {


    @Override
    public Observable<Response<VersionResult>> checkAppVersion(RequestBody body) {
        return  NetWorkManager.getRequest().getVerDataForBody(body);
    }
}
