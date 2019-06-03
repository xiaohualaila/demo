package com.aier.ardemo.ui.presenter;

import android.util.Log;
import com.aier.ardemo.network.response.ResponseTransformer;
import com.aier.ardemo.network.schedulers.BaseSchedulerProvider;
import com.aier.ardemo.ui.contract.OrderContract;
import com.aier.ardemo.ui.model.OrderModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    public void updateOrder(String username,int total,String produce_name,int pro_num,int price,String style,String material) {
        try {
        JSONObject object = new JSONObject();
        JSONObject param = new JSONObject();
        JSONArray products = new JSONArray();
        JSONObject pro = new JSONObject();
        object.put("method", "NKCLOUDAPI_UPDATEORDER");

        param.put("user_account", username);//用户
        param.put("total_price", total);//总价

        pro.put("commodity_id", "C201905230001");//商品id
        pro.put("name", produce_name);//商品名称
        pro.put("number", pro_num);//商品数量
        pro.put("price", price);//商品价格
        pro.put("socialcode", "91360782MA37WRYC1C");//
        pro.put("style", style);//款式
        pro.put("material", material);//材料
        products.put(pro);

        param.put("products", products);
        object.put("params", param);
        Log.i("xxxx object", object.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        Disposable disposable = model.updateOrder(body)
                .compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(response -> {
                    // 处理数据 直接获取到List<JavaBean> carBeans
                    view.getDataSuccess();
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
