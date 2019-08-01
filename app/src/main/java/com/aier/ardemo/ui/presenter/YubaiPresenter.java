package com.aier.ardemo.ui.presenter;

import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.URLConstant;
import com.aier.ardemo.network.request.Request;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.YubaiContract;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YubaiPresenter implements YubaiContract.Persenter{

    private YubaiContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public YubaiPresenter(YubaiContract.View view,
                          BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        mDisposable = new CompositeDisposable();

    }

    public void despose(){
        mDisposable.dispose();
    }

    @Override
    public void getYubaiData(String data) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLConstant.YUYIN_URL)
                .build();
        Request service = retrofit.create(Request.class);
        Call<YUBAIBean> call = service.getWeatherData("YUBAI",data);
        call.enqueue(new Callback<YUBAIBean>() {
            @Override
            public void onResponse(Call<YUBAIBean> call, Response<YUBAIBean> response) {
                // 已经转换为想要的类型了
                try {
                    YUBAIBean bean = response.body();
                    if(bean!=null){
                        view.getDataSuccess(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<YUBAIBean> call, Throwable t) {
                view.getDataFail();
            }

        });
    }


}
