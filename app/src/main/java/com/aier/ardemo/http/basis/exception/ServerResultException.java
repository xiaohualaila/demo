package com.aier.ardemo.http.basis.exception;


import com.aier.ardemo.http.basis.exception.base.BaseException;

/**
 * 作者：leavesC
 * 时间：2018/10/27 8:14
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class ServerResultException extends BaseException {

    public ServerResultException(int code, String message) {
        super(code, message);
    }

}
