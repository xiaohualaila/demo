package com.aier.ardemo.ui.presenter;

import android.util.Log;
import com.aier.ardemo.bean.DataBean;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.OrderContract;
import com.aier.ardemo.ui.model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OrderPresenter implements OrderContract.Persenter{

    private OrderModel model;

    private OrderContract.View view;

    private BaseSchedulerProvider schedulerProvider;

    private CompositeDisposable mDisposable;

    public OrderPresenter(OrderModel model,
                          OrderContract.View view,
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
    public void updateOrder(String username,double total,int pro_num,String produces) {
        try {
            JSONObject object = new JSONObject();
            object.put("method", "NKCLOUDAPI_UPDATEORDER");

            JSONObject param = new JSONObject();
            param.put("user_account", username);//用户
            param.put("total_price", total);//总价
            param.put("isNew", true);
            JSONArray products = new JSONArray();
            List<DataBean> list = new Gson().fromJson(produces, new TypeToken<List<DataBean>>() {}.getType());
            for (DataBean bean :list){
                if(bean.isBuy()){
                    JSONObject pro = new JSONObject();
                    pro.put("commodity_id", bean.getArkey());//商品id
                    pro.put("name", bean.getTitle());//商品名称
                    pro.put("number", bean.getNum());//商品数量
                    pro.put("price", bean.getPrice());//商品价格
                    pro.put("socialcode", "91360782MA37WRYC1C");//商家社会统一信用代码
                    pro.put("style", bean.getDesp());//款式
                    pro.put("material", "");//材料
                    products.put(pro);
                }
            }
        param.put("products", products);
        object.put("params", param);
        Log.i("xxxx object", object.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        Disposable disposable = model.updateOrder(body)
                .compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(response -> {
                    Log.i("sssss", response.toString());
                    if(response.isSuccess()){
                        view.getDataSuccess();
                    }
                }, throwable -> {
                    Log.i("sssss", throwable.toString());
                    // 处理异常
                    view.getDataFail();
                });

            mDisposable.add(disposable);
       } catch (JSONException e) {
            e.printStackTrace();
       }
    }
}
