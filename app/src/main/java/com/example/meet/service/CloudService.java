package com.example.meet.service;

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
import com.example.framework.util.ToastUtil;
import com.example.meet.persistent.preference.SharedPreferenceConstant;

public class CloudService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectServer();
    }



    private void connectServer(){

        // 用户令牌
        SharedPreferences sharedPreferences = ((BaseApplication)getApplication()).getDefaultSharedPreference();
        String token = sharedPreferences.getString(SharedPreferenceConstant.TOKEN, "");

        // 连接IM服务器
        IUserConnectionService userConnectionService = ((BackendServiceApplication) getApplication()).getBackendService(IUserConnectionService.class);
        userConnectionService.connect(token, new BackendServiceCallback<String>() {
            @Override
            public void success(String s) {
                ToastUtil.toastInDebugMode(CloudService.this, "连接成功 " + s);
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(CloudService.this, e);
            }
        });
    }

}
