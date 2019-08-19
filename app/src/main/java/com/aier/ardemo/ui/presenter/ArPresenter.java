package com.aier.ardemo.ui.presenter;


import com.aier.ardemo.bean.DataBean;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.ArContract;
import com.aier.ardemo.ui.model.ArModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ArPresenter implements ArContract.Persenter {

    private ArModel model;

    private ArContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public ArPresenter(ArModel model,
                       ArContract.View view,
                       BaseSchedulerProvider schedulerProvider) {
        this.model = model;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        mDisposable = new CompositeDisposable();
    }

    public void despose() {
        mDisposable.dispose();
    }


    @Override
    public void getArListData() {
        try {
            JSONObject object = new JSONObject();
            JSONObject obj1 = new JSONObject();
            object.put("method", "NKCLOUDAPI_GETVRMENULIST");
            object.put("params", obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
            Disposable disposable = model.getArListData(body)
                    .compose(ResponseTransformer.handleResult())
                    .compose(schedulerProvider.applySchedulers())
                    .subscribe(response -> {
                        //       Log.i("ccc","------> " + response.string());
                        if (response.isSuccess()) {
                            List<DataBean> ls = response.getData();
                            if (ls.size() < 9) {
                                int n = 9 - ls.size();
                                for (int i = 0; i < n; i++) {
                                    ls.add(new DataBean());
                                }
                            }
                            view.backArList(ls);
                        }
                    }, throwable -> {
                        // 处理异常
                        view.backDataFail("网络请求失败！");
                    });

            mDisposable.add(disposable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
