package com.aier.ardemo.ui.contract;

import com.aier.ardemo.bean.ArListBean;
import com.aier.ardemo.network.response.Response;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;


/**
 * Created by 少华 on 2019/7/31.
 */

public class ArContract {

    public interface Persenter {
         void getArListData();
    }

    public interface View {
        void backArList(List list);
        void backDataFail(String error);
    }

    public interface Model {
         Observable<Response<ArListBean>> getArListData(RequestBody body);
    }

}
