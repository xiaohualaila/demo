package com.aier.ardemo.http.datasource.base;


import com.aier.ardemo.http.basis.callback.RequestCallback;

/**
 * 作者：leavesC
 * 时间：2019/1/30 13:00
 * 描述：
 */
public interface IFailExampleDataSource {

    void test1(RequestCallback<String> callback);

    void test2(RequestCallback<String> callback);

}
