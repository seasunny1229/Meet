package com.example.framework.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.framework.application.BaseApplication;
import com.example.framework.backend.application.BackendServiceApplication;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserConnectionService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.persistent.BaseSharedPreferenceConstant;
import com.example.framework.util.ToastUtil;

public class BaseService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectIMCloudServer();
    }



    private void connectIMCloudServer(){

        // 用户令牌
        SharedPreferences sharedPreferences = ((BaseApplication)getApplication()).getDefaultSharedPreference();
        String token = sharedPreferences.getString(BaseSharedPreferenceConstant.TOKEN, "");

        // 连接IM服务器
        IUserConnectionService userConnectionService = ((BackendServiceApplication) getApplication()).getBackendService(IUserConnectionService.class);
        userConnectionService.connect(token, new BackendServiceCallback<String>() {
            @Override
            public void success(String s) {
                ToastUtil.toastInDebugMode(BaseService.this, "成功连接到IM云服务器 " + s);
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(BaseService.this, e);
            }
        });
    }


}
