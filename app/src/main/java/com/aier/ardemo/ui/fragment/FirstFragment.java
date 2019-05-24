package com.aier.ardemo.ui.fragment;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aier.ardemo.R;
import com.aier.ardemo.adapter.CourierAdapter;
import com.aier.ardemo.adapter.ProduceAdapter;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.Produces;
import com.aier.ardemo.model.WeatherResponseBean;
import com.aier.ardemo.model.WenzhangModel;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.ui.activity.MainActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.utils.GsonUtils;
import com.aier.ardemo.utils.NetUtil;
import com.karics.library.zxing.android.CaptureActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.rv_order_info)
    RecyclerView mRecyclerView;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.rv_courier)
    RecyclerView rv_courier;
    @BindView(R.id.tv_toutiao_content)
    TextView tv_toutiao_content;

    private MainActivity ac;
    ProduceAdapter mAdapter;
    CourierAdapter courierAdapter;
    List<Produces> list = new ArrayList();
    private  int total;
    private  int currentIndex;
    List<WenzhangModel.ResultBean.DataBean> dataBeanList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void init() {
        ac = (MainActivity) getActivity();
        initData();
        initTablayout();
        initRecycView();
    }

    private void initData() {
        if(NetUtil.isConnected(mActivity)){
            getWeatherData();
            getWenzhangData();
            getOrderData();
        }
    }

    private void initTablayout() {
        tablayout.addTab(tablayout.newTab().setText("设计"));
        tablayout.addTab(tablayout.newTab().setText("生产"));
        tablayout.addTab(tablayout.newTab().setText("物流"));
        tablayout.addTab(tablayout.newTab().setText("服务"));
        tablayout.setOnTabSelectedListener(this);

    }

    private void initRecycView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProduceAdapter(getDataOrder());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        rv_courier.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        courierAdapter = new CourierAdapter(getTab1Data());
        rv_courier.setItemAnimator(new DefaultItemAnimator());
        rv_courier.setAdapter(courierAdapter);
    }

    private List<Produces> getDataOrder() {
        List<Produces> list = new ArrayList();
        list.add(new Produces("2019-5-10 18:10:07","厂家已经发给快递公司"));
        list.add(new Produces("2019-4-27 12:10:07","厂家已经接单生产"));
        list.add(new Produces("2019-4-11 16:12:07","您购买了志林实木椅子"));
        return list;
    }

    private List<Produces> getTab1Data() {
        list.add(new Produces("2019-4-27 18:10:07","客户最终确认设计方案"));
        list.add(new Produces("2019-4-20 18:10:07","修改设计方案完成"));
        list.add(new Produces("2019-4-15 12:10:07","客户提出修改设计方案"));
        list.add(new Produces("2019-4-11 16:12:07","设计初步完成"));
        return list;
    }

    private List<Produces> getTab2Data() {
        list.add(new Produces("2019-5-20 18:10:07","您的订单已组装生成完成"));
        list.add(new Produces("2019-5-12 12:10:07","排程完成"));
        list.add(new Produces("2019-5-1 16:12:07","备料完成"));
        return list;
    }
    private List<Produces> getTab3Data() {
        list.add(new Produces("","暂无数据"));
        return list;
    }
    /**
     * 请求天气预报的数据
     * OnSuccessAndFaultSub 我只是加了错误处理和请求的loading，可以自己根据项目的业务修改
     * new OnSuccessAndFaultSub（第一个参数:成功or失败的回调，第二个参数:上下文，可以不填，控制dialog的）
     */
    private void getWeatherData() {
//        WeatherSubscribe.getWeatherDataForQuery("赣州", new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
//            @Override
//            public void onSuccess(String result) {
//                WeatherResponseBean weather = GsonUtils.fromJson(result, WeatherResponseBean.class);
//
//                Log.i("sss","sss"+weather.toString());
//                showWeatherText(weather);
//            }
//
//            @Override
//            public void onFault(String errorMsg) {
//                //失败
//                Toast.makeText(getActivity(), "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        },getActivity()));

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.WEATHER_URL)
                    .build();
            HttpApi service = retrofit.create(HttpApi.class);
            Call<ResponseBody> call = service.getWeatherDataForQuery("v1","赣州");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 已经转换为想要的类型了
                    try {
                        String str = response.body().string();
                        WeatherResponseBean weather = GsonUtils.fromJson(str, WeatherResponseBean.class);
                        showWeatherText(weather);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }


            });

    }


    //首页文章列表
    private void getWenzhangData() {
        try {
            JSONObject object =new JSONObject();
            JSONObject obj1 =new JSONObject();
            object.put("method","NKCLOUDAPI_GETARTICLIST");
            object.put("params",obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.BASE_URL)
                    .build();
            HttpApi service = retrofit.create(HttpApi.class);
            Call<ResponseBody> call = service.getDataForBody(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 已经转换为想要的类型了
                    try {
                        if( response.body()!=null){
                            String str = response.body().string();
                            Log.i("SSSS","str " +str);
                            WenzhangModel weather = GsonUtils.fromJson(str, WenzhangModel.class);
                             dataBeanList =  weather.getResult().getData();
                            if(dataBeanList.size()>0){
                                total =dataBeanList.size();
                                currentIndex= 0;
                                tv_toutiao_content.setText(dataBeanList.get(currentIndex).getTitle());
                                heartinterval();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }


            });
        }catch (Exception e){
            e.getMessage();
        }

    }


    //订单列表
    private void getOrderData() {
        try {
            JSONObject object =new JSONObject();
            JSONObject obj1 =new JSONObject();
            object.put("method","NKCLOUDAPI_GETORDERLIST");
            obj1.put("user_account","test");
            obj1.put("index",1);
            obj1.put("count",10);
            object.put("params",obj1);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString());
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLConstant.BASE_URL)
                    .build();
            HttpApi service = retrofit.create(HttpApi.class);
            Call<ResponseBody> call = service.getDataForBody(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // 已经转换为想要的类型了
                    try {
                        if( response.body()!=null){
                            String str = response.body().string();
                            Log.i("SSSS","str " +str);
                            Order order = GsonUtils.fromJson(str, Order.class);
                            if(order.isSuccess()){
                              Order.ResultBean resultBean = order.getResult();
                              Order.ResultBean.DataBeanX dataBeanX = resultBean.getData();
                                List<Order.ResultBean.DataBeanX.DataBean> dataBeanList = dataBeanX.getData();
                                if(dataBeanList.size()>0){
                                    Order.ResultBean.DataBeanX.DataBean bean = dataBeanList.get(0);

                                    bean.getOrder_id();
                                }

                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }


            });
        }catch (Exception e){
            e.getMessage();
        }

    }


    /**
     * 发送心跳数据
     */
    private void heartinterval() {
        Observable.interval(0, 10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    currentIndex ++;
                    if(currentIndex == total){
                        currentIndex =0;
                    }
                    tv_toutiao_content.setText(dataBeanList.get(currentIndex).getTitle());
                });
    }


    private void showWeatherText(WeatherResponseBean weathe) {
        List<WeatherResponseBean.DataBean> dataBeanList = weathe.getData();
        WeatherResponseBean.DataBean dataBean =  dataBeanList.get(0);
        WeatherResponseBean.DataBean dataBean_tomorrow =  dataBeanList.get(0);
        tv_city.setText(weathe.getCity());
        tv_wendu.setText(dataBean.getTem());//dataBean.getWea()天气
    }


    @OnClick({R.id.iv_check,R.id.iv_zhen,R.id.tv_vr_video,R.id.toutiao_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_check:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("from","wheather");
                startActivity(intent);
             //   startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.iv_zhen:
                if(!NetUtil.isConnected(mActivity)){
                    showToast("网络异常，请检查网络！");
                    return;
                }
                ac.goToArActivity();
                break;
            case R.id.tv_vr_video:
                ac.goToVRVideoActivity();
                break;
            case R.id.toutiao_item:
                if(dataBeanList!=null){
                    WenzhangModel.ResultBean.DataBean bean =dataBeanList.get(currentIndex);
                    ac.goToWebActivity(bean.getUrl(),bean.getTitle());
                }
                break;
        }

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//选中了tab的逻辑
        int position =tab.getPosition();
       switch (position){
           case 0:
               list.clear();
               getTab1Data();
               courierAdapter.notifyDataSetChanged();
               break;
           case 1:
               list.clear();
               getTab2Data();
               courierAdapter.notifyDataSetChanged();
               break;
           case 2:
               list.clear();
               getTab3Data();
               courierAdapter.notifyDataSetChanged();
               break;
           case 3:
               list.clear();
               getTab3Data();
               courierAdapter.notifyDataSetChanged();
               break;
       }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
