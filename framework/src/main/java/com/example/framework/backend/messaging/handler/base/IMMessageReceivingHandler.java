package com.example.framework.backend.messaging.handler.base;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMMessageReceivingHandler {

    void handleIMMessage(IMMessage imMessage);

}
