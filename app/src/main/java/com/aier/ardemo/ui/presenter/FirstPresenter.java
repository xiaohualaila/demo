package com.aier.ardemo.ui.presenter;

import android.util.Log;

import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.ApiManager;
import com.aier.ardemo.network.response.Response;
import com.aier.ardemo.ui.contract.FirstContract;
import org.json.JSONException;
import org.json.JSONObject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class FirstPresenter extends BasePresenter implements FirstContract.Persenter {

    private FirstContract.View view;

    public FirstPresenter(FirstContract.View view) {
        this.view = view; }

    @Override
    public void getWeatherData() {
        ApiManager.getInstence().getYubaiService()
                .getYUBAIData("YUBAI", "今天天气怎么样")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YUBAIBean>() {

                    @Override
                    public void onError(Throwable e) {
                        view.getDataFail();
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(YUBAIBean bean) {
                        try {
                            if (bean != null) {
                                String city = bean.getIpcity();
                                String result = bean.getResult();
                                String wendu = result.substring(result.lastIndexOf("℃") - 2, result.lastIndexOf("℃") + 1);
                            //    Log.i("sss", "+++" + wendu);
                                view.getWeatherDataSuccess(city, wendu);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void getWenzhangData() {
        try {
            JSONObject object = new JSONObject();
            JSONObject obj1 = new JSONObject();
            object.put("method", "NKCLOUDAPI_GETARTICLIST");
            object.put("params", obj1);
            //Log.i("sss",object.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            ApiManager.getInstence().getCommonService()
                    .getWenzhangDataForBody(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResultBean>>() {

                        @Override
                        public void onError(Throwable e) {
                            view.getDataFail();
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<ResultBean> result) {
                            if (result.isSuccess()) {
                                view.getWenzhangDataSuccess(result.getResult());
                            } else
                                view.getDataFail();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
