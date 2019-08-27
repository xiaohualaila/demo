package com.aier.ardemo.ui.presenter;

import com.aier.ardemo.bean.YUBAIBean;
import com.aier.ardemo.network.ApiManager;
import com.aier.ardemo.ui.contract.YubaiContract;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class YubaiPresenter extends BasePresenter implements YubaiContract.Persenter{

    private YubaiContract.View view;

    public YubaiPresenter(YubaiContract.View view) {
        this.view = view; }

    @Override
    public void getYubaiData(String data) {
        ApiManager.getInstence().getYubaiService()
                .getYUBAIData("YUBAI", data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YUBAIBean>() {

                    @Override
                    public void onError(Throwable e) {
                        view.getDataFail();
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(YUBAIBean bean) {
                        try {
                            if(bean!=null){
                                view.getDataSuccess(bean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
