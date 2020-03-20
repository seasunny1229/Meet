package com.example.framework.cloud.rongcloud.service;

import android.content.Context;
import android.text.TextUtils;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.service.IUserConnectionService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.framework.cloud.appserver.RongCloudAppServerManager;
import com.example.framework.exception.ExceptionFactory;
import com.example.framework.persistent.BaseSharedPreferenceConstant;

import io.rong.imlib.RongIMClient;

public class RongCloudUserConnectionService implements IUserConnectionService {

    private static RongCloudUserConnectionService sInstance;

    public static RongCloudUserConnectionService getInstance(IntegratedCloudServiceApplication context){
        if(sInstance == null){
            sInstance = new RongCloudUserConnectionService();
            sInstance.context = context;
        }
        return sInstance;
    }

    private IntegratedCloudServiceApplication context;

    @Override
    public void getUserToken(final BackendServiceCallback<String> backendServiceCallback){

        // get from local data
        String localizedToken = context.getSharedPreferences(BaseSharedPreferenceConstant.DEFAULT_NAME, Context.MODE_PRIVATE).getString(BaseSharedPreferenceConstant.TOKEN,"");

        // get from local
        if(!TextUtils.isEmpty(localizedToken)){
            backendServiceCallback.success(localizedToken);
        }

        // get from remote
        else {
            // params
            String id = UserManager.getInstance().getUser().getUid();
            String name = UserManager.getInstance().getUser().getNickName();
            String photoUrl = UserManager.getInstance().getUser().getPhoto();

            // app server
            RongCloudAppServerManager.getInstance().getUserToken(id, name, photoUrl, new BackendServiceCallback<String>() {
                @Override
                public void success(String s) {
                    context.getSharedPreferences(BaseSharedPreferenceConstant.DEFAULT_NAME, Context.MODE_PRIVATE).edit().putString(BaseSharedPreferenceConstant.TOKEN, s).apply();
                    backendServiceCallback.success(s);
                }

                @Override
                public void fail(BackendServiceException e) {
                    backendServiceCallback.fail(e);
                }
            });
        }
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
