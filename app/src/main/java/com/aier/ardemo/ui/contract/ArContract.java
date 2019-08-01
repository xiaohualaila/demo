package com.aier.ardemo.ui.contract;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;


/**
 * Created by 少华 on 2019/7/31.
 */

public class ArContract {

    public interface Persenter {

    }

    public interface View {

    }

    public interface Model {
         Observable<Response<ResultBean>> getArListData(RequestBody body);
    }

}
