package com.example.framework.cloud.rongcloud.service;

import com.example.framework.R;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserConnectionService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.exception.ExceptionFactory;

import io.rong.imlib.RongIMClient;

public class RongCloudUserConnectionService implements IUserConnectionService {

    private static RongCloudUserConnectionService sInstance;

    public static RongCloudUserConnectionService getInstance(){
        if(sInstance == null){
            sInstance = new RongCloudUserConnectionService();
        }
        return sInstance;
    }

    @Override
    public void connect(String token, final BackendServiceCallback<String> backendServiceCallback) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
                backendServiceCallback.success(s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                CloudExceptionHandler.handleRongIMClientException(errorCode);
                backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(errorCode.getMessage()));
            }
        });
    }
}
