package com.aier.ardemo.ui.presenter;

import android.util.Log;
import com.aier.ardemo.bean.VersionResult;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.WelContract;
import com.aier.ardemo.ui.model.WelcomeModel;
import com.aier.ardemo.utils.PackageUtil;
import org.json.JSONException;
import org.json.JSONObject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WelPresenter implements WelContract.Persenter{
    private WelcomeModel model;
    private WelContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public WelPresenter(WelcomeModel model, WelContract.View view,
                        BaseSchedulerProvider schedulerProvider) {
        this.model = model;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        mDisposable = new CompositeDisposable();

    }

    public void despose(){
        mDisposable.dispose();
    }


    @Override
    public void checkAppVersion() {
        try {
            JSONObject obj =new JSONObject();
            JSONObject obj1 =new JSONObject();
            obj.put("method","NKCLOUDAPI_GETLASTAPP");
            obj.put("params",obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj.toString());
            Disposable disposable = model.checkAppVersion(body)
                    .compose(ResponseTransformer.handleResult())
                    .compose(schedulerProvider.applySchedulers())
                    .subscribe(response -> {
                        Log.i("sssss", response.toString());
                        if(response.isSuccess()){
                           VersionResult.DataBean bean = response.getData();
                            String ver =PackageUtil.INSTANCE.getVersionName();
                            Log.i("sssss", "ver " +ver);
                           if(!bean.getVersion().equals(ver)){
                               view.updateVer(bean.getUrl());
                           }else {
                               view.toActivity();
                           }
                        }else {
                            view.toActivity();
                        }
                    }, throwable -> {
                        view.toActivity();
                    });

            mDisposable.add(disposable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
