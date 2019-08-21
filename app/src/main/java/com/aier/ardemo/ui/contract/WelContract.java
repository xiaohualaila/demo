package com.aier.ardemo.ui.contract;

import com.aier.ardemo.bean.ResultBean;
import com.aier.ardemo.bean.VersionResult;
import com.aier.ardemo.network.response.Response;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class WelContract {
    public interface Persenter {
        void checkAppVersion();
    }

    public interface View {
        void updateVer(String url);
        void toActivity();
    }

    public interface Model {
        Observable<Response<VersionResult>> checkAppVersion(RequestBody body);
    }
}
