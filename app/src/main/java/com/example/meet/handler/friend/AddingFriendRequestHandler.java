package com.example.meet.handler.friend;

import android.content.Context;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.INewFriendManagementService;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.util.LogUtil;
import com.example.meet.eventbus.EventBusConstant;
import com.example.meet.persistent.litepal.bean.NewFriend;
import com.example.meet.persistent.litepal.constant.LitePalConstant;

import java.util.List;

public class AddingFriendRequestHandler extends BaseIMMessageReceivingHandler {


    @Override
    public void handleIMMessage(final Context context, final IMMessage imMessage) {
        LogUtil.i("添加好友请求：" + imMessage.toString());

        // 查询现在已有好友
        final INewFriendManagementService service = ((IntegratedCloudServiceApplication) context).getBackendService(INewFriendManagementService.class);
        service.queryAllNewFriends(NewFriend.class, new BackendServiceCallback<List<NewFriend>>() {
            @Override
            public void success(List<NewFriend> newFriends) {

                // 不在好友列表里面
                if(newFriends == null || newFriends.isEmpty() || !hasInNewFriendList(newFriends, imMessage)){
                    LogUtil.i("添加好友");

                    // 添加新好友
                    addNewFriend(context, service, imMessage);

                    // use event bus to update UI
                    EventBusUtil.post(new MessageEvent(EventBusConstant.UPDATE_NEW_FRIEND_INFO));
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(context, e);
            }
        });

    }

    private boolean hasInNewFriendList(List<NewFriend> newFriends, IMMessage imMessage){
        String sentUserId = imMessage.getSourceId();
        for(NewFriend newFriend : newFriends){
            String friendUid = newFriend.getUid();
            if(friendUid.equals(sentUserId)){
                return true;
            }
        }
        return false;
    }

    private void  addNewFriend(final Context context, INewFriendManagementService service, IMMessage imMessage){
        NewFriend newFriend = new NewFriend();
        newFriend.setUid(imMessage.getSourceId());
        newFriend.setMessage(((IMTextMessage)imMessage).getMessageContent());
        newFriend.setTime(System.currentTimeMillis());
        newFriend.setStatus(LitePalConstant.NEW_FRIEND_STATUS_TO_BE_CONFIRMED);
        service.addNewFriend(newFriend, new BackendServiceCallback<NewFriend>() {
            @Override
            public void success(NewFriend newFriend) {
                LogUtil.i("添加好友成功");
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(context, e);
            }
        });
    }


}
