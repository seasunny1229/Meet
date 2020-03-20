package com.example.framework.cloud.appserver;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.exception.ExceptionFactory;

import io.rong.RongCloud;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;
import io.rong.util.RcloudServiceConstants;

public class RongCloudAppServerManager {

    private static RongCloudAppServerManager sInstance;

    public static RongCloudAppServerManager getInstance(){
        if(sInstance == null){
            sInstance = new RongCloudAppServerManager();
        }
        return sInstance;
    }

    private RongCloud rongCloud;


    public  void init(String rongCloudAppKey, String rongCloudAppSecret){
        rongCloud = RongCloud.getInstance(rongCloudAppKey, rongCloudAppSecret);
    }

    public void getUserToken(String objectId, String name, String portraitUrl, BackendServiceCallback<String> backendServiceCallback){
        UserModel userModel = new UserModel()
                .setId(objectId)
                .setName(name)
                .setPortrait(portraitUrl);
        try {
            TokenResult result = rongCloud.user.register(userModel);
            if (result.getCode() == RcloudServiceConstants.HTTP_OK) {
                backendServiceCallback.success(result.getToken());
            }
            else if(result.getCode() == RcloudServiceConstants.RCLOUD_TESTUSERS_OUT){
                CloudExceptionHandler.handlerRongCloudServerException(result);
                backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
            }
        }catch (Exception e){
            CloudExceptionHandler.handleRongCloudServerException(e);
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
        }
    }

}
