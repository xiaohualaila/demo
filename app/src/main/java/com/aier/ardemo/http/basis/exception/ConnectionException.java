package com.aier.ardemo.http.basis.exception;

import com.aier.ardemo.http.basis.config.HttpCode;
import com.aier.ardemo.http.basis.exception.base.BaseException;

/**
 * 作者：leavesC
 * 时间：2018/10/25 21:37
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class ConnectionException extends BaseException {

    public ConnectionException() {
        super(HttpCode.CODE_CONNECTION_FAILED, "网络请求失败");
    }

}
