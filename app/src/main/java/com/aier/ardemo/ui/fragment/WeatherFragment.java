package com.aier.ardemo.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.aier.ardemo.R;
import com.aier.ardemo.adapter.ProduceAdapter;
import com.aier.ardemo.model.Produces;
import com.aier.ardemo.model.WeatherResponseBean;
import com.aier.ardemo.netapi.HttpApi;
import com.aier.ardemo.netapi.URLConstant;
import com.aier.ardemo.netsubscribe.WeatherSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.activity.MainActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.utils.GsonUtils;
import com.aier.ardemo.utils.NetUtil;
import com.karics.library.zxing.android.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class WeatherFragment extends BaseFragment {
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.iv_weather)
    ImageView iv_weather;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.rv_order_info)
    RecyclerView mRecyclerView;

    private MainActivity ac;
    ProduceAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void init() {
        ac = (MainActivity) getActivity();
        getWeatherData();
        initRecycView();
    }

    private void initRecycView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ProduceAdapter(getDataOrder());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Produces> getDataOrder() {
        List<Produces> list = new ArrayList();
        list.add(new Produces("2019-4-11 16:12:07","您购买了志林实木椅子"));
        list.add(new Produces("2011-4-27 12:10:07","厂家已经接单生产"));
        list.add(new Produces("2011-4-27 18:10:07","厂家已经发货"));
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

    private void showWeatherText(WeatherResponseBean weathe) {
        List<WeatherResponseBean.DataBean> dataBeanList = weathe.getData();
        WeatherResponseBean.DataBean dataBean =  dataBeanList.get(0);
        WeatherResponseBean.DataBean dataBean_tomorrow =  dataBeanList.get(0);
        tv_city.setText(weathe.getCity());
        tv_wendu.setText(dataBean.getTem());//dataBean.getWea()天气






    }


    @OnClick({R.id.iv_check,R.id.iv_zhen,R.id.tv_vr_video})
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
        }

    }



}
