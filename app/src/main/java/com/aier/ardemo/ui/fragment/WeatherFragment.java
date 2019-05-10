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
import com.aier.ardemo.netsubscribe.WeatherSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.activity.MainActivity;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.utils.GsonUtils;
import com.karics.library.zxing.android.CaptureActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

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


    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

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

        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ac, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new ProduceAdapter(getDataOrder());
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Produces> getDataOrder() {
        List<Produces> list = new ArrayList();
        list.add(new Produces("2011-9-11 12:10:07","xxxxxxxxxxxxxxxxx"));
        list.add(new Produces("2011-4-11 12:10:07","hhhhhhhhhhhhhhhhhh"));
        list.add(new Produces("2017-4-11 12:10:07","sssssssss"));
        list.add(new Produces("2017-4-11 12:10:07","qqqqqqqqqqqqq"));
        return list;
    }


    /**
     * 请求天气预报的数据
     * OnSuccessAndFaultSub 我只是加了错误处理和请求的loading，可以自己根据项目的业务修改
     * new OnSuccessAndFaultSub（第一个参数:成功or失败的回调，第二个参数:上下文，可以不填，控制dialog的）
     */
    private void getWeatherData() {
        WeatherSubscribe.getWeatherDataForBody("", new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                WeatherResponseBean weather = GsonUtils.fromJson(result, WeatherResponseBean.class);

                Log.i("sss","sss"+weather.toString());
                showWeatherText(weather);
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(getActivity(), "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        },getActivity()));
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
                Intent intent = new Intent(new Intent(getActivity(), CaptureActivity.class));
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

              //  qrCoded.setText("解码结果： \n" + content);
            //    Log.i("sss",content);
            //    qrCodeImage.setImageBitmap(bitmap);
            }
        }
    }


}
