package com.example.framework.cloud.rongcloud.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.messaging.converter.IMConverter;
import com.example.framework.backend.messaging.handler.manager.MessageHandlerManager;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.rongcloud.converter.factory.MessageFactory;
import com.example.framework.cloud.rongcloud.converter.filter.IMMessageFilterManager;
import com.example.framework.cloud.rongcloud.converter.manager.RongCloudMessageConverterManager;
import com.example.framework.cloud.rongcloud.converter.migration.MessageMigrationUtil;
import com.example.framework.exception.ExceptionFactory;
import com.example.framework.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class RongCloudMessageService implements IMessageService {

    // constant
    private static final int DEFAULT_OLDEST_MESSAGE_ID = -1, DEFAULT_HISTORY_MESSAGES_COUNT = 1000;


    private static RongCloudMessageService sInstance;

    public static RongCloudMessageService getInstance(){
        if(sInstance == null){
            sInstance = new RongCloudMessageService();
        }
        return sInstance;
    }

    private IMConverter<Message> converter;

    private IMMessageFilterManager imMessageFilterManager;


    private RongCloudMessageService(){
        converter = new RongCloudMessageConverterManager();
        imMessageFilterManager = new IMMessageFilterManager()
                .add(MessageHandlerManager.getInstance().getHandlerFilter(MessageHandlerManager.MessageHandlerFilterType.chat));
    }

    @Override
    public void sendMessage(IMMessage imMessage, final BackendServiceCallback<Object> backendServiceCallback) {
        Message message = new Message();
        converter.convertToRemoteMessage(imMessage, message);
        RongIMClient.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                LogUtil.i("发送成功：" + message.toString());
                if(backendServiceCallback != null){
                    backendServiceCallback.success(message);
                }
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                CloudExceptionHandler.handleRongIMClientException(errorCode);
                if(backendServiceCallback != null){
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
                }
            }
        });
    }

    @Override
    public void receiveMessage(final BackendServiceCallback<Object> backendServiceCallback) {
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                LogUtil.i("接收成功：" + message.toString());
                IMMessage imMessage = MessageFactory.createIMMessage(message.getContent());
                converter.convertToLocalMessage(imMessage, message);
                backendServiceCallback.success(imMessage);
                return true;
            }
        });
    }

    @Override
    public void getLocalPrivateHistoryMessages(String targetId, final BackendServiceCallback<List<IMMessage>> backendServiceCallback) {
        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, targetId, DEFAULT_OLDEST_MESSAGE_ID, DEFAULT_HISTORY_MESSAGES_COUNT, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                List<IMMessage> imMessages = new ArrayList<>();
                for(Message message : messages){

                    // the permanent messages need migration checking
                    if(!MessageMigrationUtil.isMessageVersionValid((RongCloudMessageConverterManager)converter, message.getContent())){
                        MessageMigrationUtil.migrateExtraInfo((RongCloudMessageConverterManager)converter, message);
                    }

                    // convert to local IM message
                    IMMessage imMessage = MessageFactory.createIMMessage(message.getContent());
                    converter.convertToLocalMessage(imMessage, message);

                    // filter message
                    if(!imMessageFilterManager.isValid(imMessage)){
                        continue;
                    }

                    imMessages.add(imMessage);
                }
                backendServiceCallback.success(imMessages);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                    CloudExceptionHandler.handleRongIMClientException(errorCode);
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
            }
        });
    }
}
