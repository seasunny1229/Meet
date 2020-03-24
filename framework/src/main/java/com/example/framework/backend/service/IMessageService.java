package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.messaging.message.IMMessage;

import java.util.List;

public interface IMessageService {

    // 发送消息
    void sendMessage(IMMessage imMessage, BackendServiceCallback<Object> backendServiceCallback);

    // 接收到消息
    void receiveMessage(BackendServiceCallback<Object> backendServiceCallback);

    // 得到本地历史消息
    void getLocalPrivateHistoryMessages(String targetId, BackendServiceCallback<List<IMMessage>> backendServiceCallback);

}
