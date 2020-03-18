package com.example.meet.application;

import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.meet.BuildConfig;
import com.example.meet.backend.BackendServiceConfig;


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
}
