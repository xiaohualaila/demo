package com.aier.ardemo.ui.contract;
import com.aier.ardemo.bean.OrderResultBean;
import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class OrderContract {

    public interface Persenter {
        void updateOrder(String username,double total,String produce_name,int pro_num,double price,String style,String material);

    }

    public interface View {
        void getDataSuccess();
        void getDataFail();
    }

    public interface Model {
         Observable<Response<OrderResultBean>> updateOrder(RequestBody body);
    }

}
