package com.example.meet.handler.friend;

import android.content.Context;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.framework.exception.ExceptionHandler;

public class AddingFriendAgreeHandler extends BaseIMMessageReceivingHandler {


    @Override
    public void handleIMMessage(Context context, IMMessage imMessage) {

        // context
        final IntegratedCloudServiceApplication application = (IntegratedCloudServiceApplication) context;

        // update remote friend list
        IFriendManagementService friendManagementService = application.getBackendService(IFriendManagementService.class);
        friendManagementService.addFriend(imMessage.getTargetId(), imMessage.getSourceId(), new BackendServiceCallback<Void>() {
            @Override
            public void success(Void aVoid) {

            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(application, e);
            }
        });

    }
}
