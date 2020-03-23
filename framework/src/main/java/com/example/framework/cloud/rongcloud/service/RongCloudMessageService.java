package com.example.framework.cloud.rongcloud.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.messaging.converter.IMConverter;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.rongcloud.converter.factory.MessageFactory;
import com.example.framework.cloud.rongcloud.converter.manager.RongCloudMessageConverterManager;
import com.example.framework.exception.ExceptionFactory;
import com.example.framework.util.LogUtil;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

public class RongCloudMessageService implements IMessageService {


    private static RongCloudMessageService sInstance;

    public static RongCloudMessageService getInstance(){
        if(sInstance == null){
            sInstance = new RongCloudMessageService();
        }
        return sInstance;
    }

    private IMConverter<Message> converter;

    private RongCloudMessageService(){
        converter = new RongCloudMessageConverterManager();
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
}
