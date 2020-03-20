package com.example.framework.backend.service;

import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.callback.BackendServiceCallback;

public interface IMessageService {

    // 发送消息
    void sendMessage(IMMessage imMessage, BackendServiceCallback<Object> backendServiceCallback);

    // 接收到消息
    void receiveMessage(BackendServiceCallback<Object> backendServiceCallback);

}
