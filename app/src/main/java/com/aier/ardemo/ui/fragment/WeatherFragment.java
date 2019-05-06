package com.aier.ardemo.ui.fragment;


import android.util.Log;
import android.widget.Toast;
import com.aier.ardemo.R;
import com.aier.ardemo.model.WeatherResponseBean;
import com.aier.ardemo.netsubscribe.WeatherSubscribe;
import com.aier.ardemo.netutils.OnSuccessAndFaultListener;
import com.aier.ardemo.netutils.OnSuccessAndFaultSub;
import com.aier.ardemo.ui.base.BaseFragment;
import com.aier.ardemo.utils.GsonUtils;


public class WeatherFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void init() {
        getWeatherData();
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
                //成功
                Toast.makeText(getActivity(), "请求成功~", Toast.LENGTH_SHORT).show();
                WeatherResponseBean weather = GsonUtils.fromJson(result, WeatherResponseBean.class);

                Log.i("sss","sss"+weather.toString());
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(getActivity(), "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        },getActivity()));
    }


}
