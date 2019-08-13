package com.aier.ardemo.ui.presenter;

import android.util.Log;
import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.URLConstant;
import com.aier.ardemo.network.request.Request;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.ui.model.FirstModel;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.FirstContract;
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

public class FirstPresenter implements FirstContract.Persenter{

    private FirstModel model;

    private FirstContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public FirstPresenter(FirstModel model,
                          FirstContract.View view,
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
    public void getWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLConstant.YUYIN_URL)
                .build();
        Request service = retrofit.create(Request.class);
        Call<YUBAIBean> call = service.getWeatherData("YUBAI","今天天气怎么样");
        call.enqueue(new Callback<YUBAIBean>() {
            @Override
            public void onResponse(Call<YUBAIBean> call, Response<YUBAIBean> response) {
                // 已经转换为想要的类型了
                try {
                    YUBAIBean bean = response.body();
                    if(bean!=null){
                        String city = bean.getIpcity();
                        String result = bean.getResult();
                        String wendu = result.substring(result.lastIndexOf("℃")-2,result.lastIndexOf("℃")+1);
                      //  Log.i("sss","+++"+wendu);
                        view.getWeatherDataSuccess(city,wendu);
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

    @Override
    public void getWenzhangData() {

        try {
            JSONObject object =new JSONObject();
            JSONObject obj1 =new JSONObject();
            object.put("method","NKCLOUDAPI_GETARTICLIST");
            object.put("params",obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
            Disposable disposable = model.getWenzhangData(body)
                    .compose(ResponseTransformer.handleResult())
                    .compose(schedulerProvider.applySchedulers())
                    .subscribe(response -> {
                     //   Log.i("sss","------> " + response.toString());
                        // 处理数据 直接获取到List<JavaBean> carBeans
                        view.getWenzhangDataSuccess(response);
                    }, throwable -> {
                        // 处理异常
                        view.getDataFail();
                    });

            mDisposable.add(disposable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
