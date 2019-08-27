package com.aier.ardemo.ui.presenter;

import android.util.Log;

import com.aier.ardemo.bean.VersionResult;
import com.aier.ardemo.network.ApiManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.WelContract;
import com.aier.ardemo.utils.PackageUtil;
import org.json.JSONException;
import org.json.JSONObject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WelPresenter extends BasePresenter implements WelContract.Persenter{

    private WelContract.View view;

    public WelPresenter(WelContract.View view) {
        this.view = view;
    }


    @Override
    public void checkAppVersion() {
        try {
            JSONObject obj = new JSONObject();
            JSONObject obj1 = new JSONObject();
            obj.put("method", "NKCLOUDAPI_GETLASTAPP");
            obj1.put("appname", "南康家居防伪");
            obj.put("params", obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
            ApiManager.getInstence().getCommonService()
                    .getVerDataForBody(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<VersionResult>>() {
                        @Override
                        public void onError(Throwable e) {
                            view.toActivity();
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<VersionResult> response) {
//                               Log.i("sssss", response.toString());
                            if (response.isSuccess()) {
                                VersionResult result = response.getResult();
                                VersionResult.DataBean bean = result.getData();
                                String ver = PackageUtil.INSTANCE.getVersionName();
                                if (!bean.getVersion().equals(ver)) {
                                    view.updateVer(bean.getUrl());
                                } else {
                                    view.toActivity();
                                }
                            } else {
                                view.toActivity();
                            }
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
