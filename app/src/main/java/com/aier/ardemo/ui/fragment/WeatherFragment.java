package com.aier.ardemo.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import static android.app.Activity.RESULT_OK;


public class WeatherFragment extends BaseFragment {
    @BindView(R.id.tv_city)
    TextView tv_city;
//    @BindView(R.id.tv_time)
//    TextView tv_time;
    @BindView(R.id.tv_wendu)
    TextView tv_wendu;
    @BindView(R.id.tv_tianqi)
    TextView tv_tianqi;
    @BindView(R.id.tv_to_tianqi)
    TextView tv_to_tianqi;
    @BindView(R.id.tv_to_tem1)
    TextView tv_to_tem1;
    @BindView(R.id.tv_to_tem2)
    TextView tv_to_tem2;
    @BindView(R.id.tv_shidu_)
    TextView tv_shidu_;
    @BindView(R.id.tv_fengli_)
    TextView tv_fengli_;
    @BindView(R.id.tv_ye_tianqi)
    TextView tv_ye_tianqi;
    @BindView(R.id.tv_ye_tem1)
    TextView tv_ye_tem1;
    @BindView(R.id.tv_ye_tem2)
    TextView tv_ye_tem2;
    @BindView(R.id.tv_air_)
    TextView tv_air_;
    @BindView(R.id.tv_air_quality_)
    TextView tv_air_quality_;
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
//        tv_time.setText(weathe.getUpdate_time());
        tv_wendu.setText(dataBean.getTem());
        tv_tianqi.setText(dataBean.getWea());
        tv_to_tianqi.setText(dataBean.getWea());
        tv_to_tem1.setText(dataBean.getTem2());
        tv_to_tem2.setText(dataBean.getTem1());
        tv_shidu_.setText(dataBean.getHumidity()+"");

        List<String> strings =dataBean.getWin();
        if(strings.size()>0){
            tv_fengli_.setText(dataBean.getWin_speed()+" "+strings.get(0));
        }
        tv_air_.setText(dataBean.getAir()+"");
        tv_air_quality_.setText(dataBean.getAir_level());
        tv_ye_tem1.setText(dataBean_tomorrow.getTem2());
        tv_ye_tem2.setText(dataBean_tomorrow.getTem2());

    }


    @OnClick({R.id.tv_check,R.id.tv_zhen,R.id.tv_vr_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("from","wheather");
                startActivity(intent);
             //   startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.tv_zhen:
                ac.goToArActivity();
                break;
            case R.id.tv_vr_video:
                ac.goToVRVideoActivity();
                break;
        }

    }



}
