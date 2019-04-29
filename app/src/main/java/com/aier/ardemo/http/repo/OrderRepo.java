package com.aier.ardemo.http.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.aier.ardemo.http.basis.BaseRepo;
import com.aier.ardemo.http.datasource.base.ILoginDataSource;
import com.aier.ardemo.http.datasource.base.IOrderDataSource;
import com.aier.ardemo.model.Order;
import com.aier.ardemo.model.Weather;


/**
 * 作者：leavesC
 * 时间：2018/10/27 21:12
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class OrderRepo extends BaseRepo<IOrderDataSource> {

    public OrderRepo(IOrderDataSource remoteDataSource) {
        super(remoteDataSource);
    }

    public MutableLiveData<Weather> createOrder(String text) {
        MutableLiveData<Weather> liveData = new MutableLiveData<>();
        remoteDataSource.createOrder(text, data -> {
            liveData.setValue(data);
        });
        return liveData;
    }


}