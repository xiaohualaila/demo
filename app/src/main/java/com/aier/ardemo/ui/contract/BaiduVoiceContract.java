package com.aier.ardemo.ui.contract;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;


/**
 * Created by Zaifeng on 2018/3/1.
 */

public class BaiduVoiceContract {

    public interface Persenter {
        void getYubaiData(String queryData);

    }

    public interface View {
        void getDataSuccess(YUBAIBean bean);
        void getDataFail();
    }

    public interface Model {

    }

}
