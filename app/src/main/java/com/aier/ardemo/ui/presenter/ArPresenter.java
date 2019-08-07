package com.aier.ardemo.ui.presenter;

import android.util.Log;

import com.aier.ardemo.bean.ArBean;
import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.URLConstant;
import com.aier.ardemo.network.request.Request;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.ArContract;
import com.aier.ardemo.ui.contract.FirstContract;
import com.aier.ardemo.ui.model.ArModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
//        try {
//            JSONObject object =new JSONObject();
//            JSONObject obj1 =new JSONObject();
//            object.put("method","");
//            object.put("params",obj1);
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
//            Disposable disposable = model.getArListData(body)
//               //     .compose(ResponseTransformer.handleResult())
//                    .compose(schedulerProvider.applySchedulers())
//                    .subscribe(response -> {
//                        Log.i("sss","------> " + response.toString());
//                        // 处理数据 直接获取到List<JavaBean> carBeans
//
//                    }, throwable -> {
//                        // 处理异常
//                        view.backDataFail(throwable.getMessage());
//                    });
//
//            mDisposable.add(disposable);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        List list = new ArrayList();
        list.add(new ArBean("10302581", "红橡木",""));
        list.add(new ArBean("10302537", "黑胡桃",""));
        list.add(new ArBean("10302518", "白腊木",""));
        list.add(new ArBean("10307873", "婴儿车",""));

        list.add(new ArBean("", "",""));
        list.add(new ArBean("", "",""));
        list.add(new ArBean("", "",""));
        list.add(new ArBean("", "",""));
        list.add(new ArBean("", "",""));


        view.backArList(list);
    }
}
