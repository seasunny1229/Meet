package com.example.framework.cloud.rongcloud.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.messaging.conversation.IMConversation;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IConversationService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.rongcloud.converter.extra.ExtraInfoUtil;
import com.example.framework.cloud.rongcloud.converter.factory.MessageFactory;
import com.example.framework.exception.ExceptionFactory;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class RongCloudConversationService implements IConversationService {

    private static RongCloudConversationService sInstance;

    public static RongCloudConversationService getInstance(){
        if(sInstance == null){
            sInstance = new RongCloudConversationService();
        }
        return sInstance;
    }


    @Override
    public void getConversationList(final BackendServiceCallback<List<IMConversation>> backendServiceCallback) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<IMConversation> imConversations = new ArrayList<>();
                for(Conversation conversation : conversations){
                    IMConversation imConversation = new IMConversation();
                    imConversation.setConversationType(ExtraInfoUtil.convertToIMConversationType(conversation));
                    imConversation.setTargetId(conversation.getTargetId());
                    imConversation.setReceivedTime(conversation.getReceivedTime());
                    imConversation.setUnreadMessageCount(conversation.getUnreadMessageCount());
                    imConversation.setLatestIMMessage(convertToIMMessage(conversation.getLatestMessage()));
                    imConversations.add(imConversation);
                }
                backendServiceCallback.success(imConversations);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                CloudExceptionHandler.handleRongIMClientException(errorCode);
                backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
            }
        });
    }


    private IMMessage convertToIMMessage(MessageContent messageContent){
        IMMessage imMessage = MessageFactory.createIMMessage(messageContent);
        if(imMessage instanceof IMTextMessage){
            ((IMTextMessage) imMessage).setMessageContent(((TextMessage)messageContent).getContent());
        }
        return imMessage;
    }

}
