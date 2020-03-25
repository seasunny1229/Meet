package com.example.meet.application;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.handler.manager.MessageHandlerManager;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.meet.BuildConfig;
import com.example.meet.backend.BackendServiceConfig;
import com.example.meet.handler.chat.ChatHandler;
import com.example.meet.handler.friend.AddingFriendAgreeHandler;
import com.example.meet.handler.friend.AddingFriendRequestHandler;


public class MainApplication extends IntegratedCloudServiceApplication {

    @Override
    protected String getAppId() {
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    protected String getBmobApplicationId() {
        return BackendServiceConfig.BMOB_APPLICATION_ID;
    }

    @Override
    protected String getRongCloudAppKey() {
        return BackendServiceConfig.RONG_CLOUD_KEY;
    }

    @Override
    protected String getRongCloudAppSecret() {
        return BackendServiceConfig.RONG_CLOUD_SECRET;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init message handlers
        MessageHandlerManager.getInstance().init(new BaseIMMessageReceivingHandler[]{
                new AddingFriendRequestHandler(),
                new AddingFriendAgreeHandler(),
                new ChatHandler()
        });
    }
}
