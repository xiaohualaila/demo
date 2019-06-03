package com.aier.ardemo.ui.contract;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * Created by Zaifeng on 2018/3/1.
 */

public class FirstContract {

    public interface Persenter {
        void getWeatherData();
        void getWenzhangData();
    }

    public interface View {
        void getWeatherDataSuccess(String city,String wendu);
        void getWenzhangDataSuccess(ResultBean bean);
        void getDataFail();
    }

    public interface Model {
         Observable<Response<ResultBean>> getWenzhangData(RequestBody body);
    }

}
