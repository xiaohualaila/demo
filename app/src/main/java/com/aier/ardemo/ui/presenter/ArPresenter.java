package com.aier.ardemo.ui.presenter;


import com.aier.ardemo.bean.ArListBean;
import com.aier.ardemo.bean.DataBean;
import com.aier.ardemo.network.ApiManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.ArContract;
import com.aier.ardemo.ui.model.ArModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ArPresenter extends BasePresenter implements ArContract.Persenter {

    private ArContract.View view;

    public ArPresenter(ArContract.View view) {
        this.view = view;
    }

    @Override
    public void getArListData() {

        try {
            JSONObject object = new JSONObject();
            JSONObject obj1 = new JSONObject();
            object.put("method", "NKCLOUDAPI_GETVRMENULIST");
            object.put("params", obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            ApiManager.getInstence().getCommonService()
                    .getArListData(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ArListBean>>() {
                        @Override
                        public void onError(Throwable e) {
                            view.backDataFail("网络请求失败！");
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<ArListBean> response) {
//                               Log.i("sssss", response.toString());
                            if (response.isSuccess()) {
                                ArListBean result = response.getResult();
                                List<DataBean> ls = result.getData();
                                if (ls.size() < 9) {
                                    int n = 9 - ls.size();
                                    for (int i = 0; i < n; i++) {
                                        ls.add(new DataBean());
                                    }
                                }
                                view.backArList(ls);
                            }
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
