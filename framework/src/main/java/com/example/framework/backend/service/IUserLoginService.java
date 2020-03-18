package com.example.framework.backend.service;

import android.content.Context;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.marker.IUser;

/**
 *
 * 在登录页面获取请求时使用
 *
 */

public interface IUserLoginService<T extends IUser> {

    // 获取验证码，以字符串形式返回
    void requestSms(String phone, BackendServiceCallback<String> backendServiceCallback);

    // 验证码输入错误，或者验证码以过期失效
    boolean isLoginFailedByWrongVerificationCode(Context context, BackendServiceException e);

    // 通过手机验证码登录，并且获取登录用户对象
    void loginByPhone(String phone, String verificationCode, BackendServiceCallback<T> backendServiceCallback);

    // 通过用户名和密码登录，并且获取登录用户对象
    void loginByAccount(String account, String password, BackendServiceCallback<T> backendServiceCallback);

    // TODO: 通过第三方登录

}



