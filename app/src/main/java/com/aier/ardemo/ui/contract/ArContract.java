package com.aier.ardemo.ui.contract;


import com.aier.ardemo.bean.DataBean;
import java.util.List;


/**
 * Created by 少华 on 2019/7/31.
 */

public class ArContract {

    public interface Persenter {
         void getArListData(boolean isChild,String id);
    }

    public interface View {
        void backArList(List<DataBean> list);
        void backDataFail(String error);

        void backArChildList(List<DataBean> list);
    }


}
