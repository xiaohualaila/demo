package com.aier.ardemo.ui.contract;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


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
         Observable<ResponseBody> getArListData(RequestBody body);
    }

}
