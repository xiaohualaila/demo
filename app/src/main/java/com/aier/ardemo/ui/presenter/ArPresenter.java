package com.aier.ardemo.ui.presenter;

import android.util.Log;

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

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArPresenter implements ArContract.Persenter{

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

    public void despose(){
        mDisposable.dispose();
    }




}
