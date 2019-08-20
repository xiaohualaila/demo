package com.aier.ardemo.ui.contract;
import com.aier.ardemo.bean.CommonResult;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class OrderContract {

    public interface Persenter {
        void updateOrder(String username,double total,int pro_num,String produces);

    }

    public interface View {
        void getDataSuccess();
        void getDataFail();
    }

    public interface Model {
         Observable<Response<CommonResult>> updateOrder(RequestBody body);
    }

}
