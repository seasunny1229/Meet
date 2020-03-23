package com.example.framework.cloud.rongcloud.converter.factory;

import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class MessageFactory {

    public static IMMessage createIMMessage(MessageContent messageContent){
        IMMessage imMessage = null;
        if(messageContent.getClass() == TextMessage.class){
            imMessage = new IMTextMessage();
        }
        return imMessage;
    }

}
