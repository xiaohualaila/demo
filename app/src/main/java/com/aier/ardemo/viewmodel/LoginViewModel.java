package com.aier.ardemo.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.aier.ardemo.viewmodel.base.BaseViewModel;
import com.aier.ardemo.http.datasource.LoginDataSource;
import com.aier.ardemo.http.repo.LoginRepo;
import com.aier.ardemo.model.QrCode;


/**
 * 作者：leavesC
 * 时间：2018/10/27 21:14
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class LoginViewModel extends BaseViewModel {

    private MutableLiveData<QrCode> loginLiveData;

    private LoginRepo loginRepo;

    public LoginViewModel() {
        loginLiveData = new MutableLiveData<>();
        loginRepo = new LoginRepo(new LoginDataSource(this));
    }

    public void createQrCode(String text, int width) {
        loginRepo.createQrCode(text, width).observe(lifecycleOwner, qrCode -> loginLiveData.setValue(qrCode));
    }

    public MutableLiveData<QrCode> getloginLiveData() {
        return loginLiveData;
    }

}
