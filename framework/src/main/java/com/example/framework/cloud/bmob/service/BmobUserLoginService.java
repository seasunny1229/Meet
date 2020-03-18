package com.example.framework.cloud.bmob.service;

import android.content.Context;

import com.example.framework.R;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserLoginService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

public class BmobUserLoginService implements IUserLoginService<User> {


    private static BmobUserLoginService sInstance;

    public static BmobUserLoginService getInstance(){
        if(sInstance == null){
            sInstance = new BmobUserLoginService();
        }
        return sInstance;
    }


    @Override
    public void requestSms(String phone, final BackendServiceCallback<String> backendServiceCallback) {
        BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    backendServiceCallback.success(String.valueOf(integer));
                }
                else {
                    backendServiceCallback.fail(new BackendServiceException(e));
                }
            }
        });
    }

    @Override
    public boolean isLoginFailedByWrongVerificationCode(Context context, BackendServiceException e) {
        return e.getErrorCode() == context.getResources().getInteger(R.integer.sms_code_input_wrong_verification_code);
    }

    @Override
    public void loginByPhone(String phone, String verificationCode, final BackendServiceCallback<User> backendServiceCallback) {
        BmobUser.signOrLoginByMobilePhone(phone, verificationCode, new LogInListener<IMBmobUser>() {
            @Override
            public void done(IMBmobUser imBmobUser, BmobException e) {

                // 登录成功
                if(e == null){
                    backendServiceCallback.success(null);
                }

                // 登录失败
                else {
                    BackendServiceException backendServiceException = new BackendServiceException(e);
                    backendServiceException.setErrorCode(e.getErrorCode());
                    backendServiceCallback.fail(backendServiceException);
                }
            }
        });
    }

    @Override
    public void loginByAccount(String account, String password, final BackendServiceCallback<User> backendServiceCallback) {
        BmobUser.loginByAccount(account, password, new LogInListener<IMBmobUser>() {
            @Override
            public void done(IMBmobUser imBmobUser, BmobException e) {

                // 登录成功
                if(e == null){
                    backendServiceCallback.success(null);
                }

                // 登录失败
                else {
                    BackendServiceException backendServiceException = new BackendServiceException(e);
                    backendServiceException.setErrorCode(e.getErrorCode());
                    backendServiceCallback.fail(backendServiceException);
                }
            }
        });


    }

}
