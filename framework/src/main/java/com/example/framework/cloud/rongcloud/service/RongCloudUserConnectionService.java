package com.example.framework.cloud.rongcloud.service;

import android.app.Application;
import android.content.Context;

import com.example.framework.R;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserConnectionService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.rongcloud.constant.RongCloudResponseCode;
import com.example.framework.exception.ExceptionFactory;

import cn.bmob.v3.BmobUser;
import io.rong.RongCloud;
import io.rong.imlib.RongIMClient;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

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
    public void getUserToken(BackendServiceCallback<String> backendServiceCallback){
        IMBmobUser imBmobUser = BmobUser.getCurrentUser(IMBmobUser.class);
        UserModel userModel = new UserModel()
                .setId(imBmobUser.getObjectId())
                .setName(imBmobUser.getTokenNickName())
                .setPortrait(imBmobUser.getTokenPhoto());
        RongCloud rongCloud = context.getRongCloud();
        try {
            TokenResult result = rongCloud.user.register(userModel);
            if (result.getCode() == context.getResources().getInteger(R.integer.http_response_code_ok)) {
                backendServiceCallback.success(result.getToken());
            }
            else if(result.getCode() == RongCloudResponseCode.NUM_USERS_MEET_UPPER_LIMIT){
                CloudExceptionHandler.handlerRongCloudServerException(result);
                backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
            }
        }catch (Exception e){
            CloudExceptionHandler.handleRongCloudServerException(e);
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
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
