package com.example.framework.cloud.rongcloud.converter.extra;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.message.IMMessageType;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class ExtraInfoUtil {

    public static IMMessageType convertToIMMessageType(Message message){
        IMMessageType imMessageType = null;
        if(message.getContent() instanceof TextMessage){
            imMessageType = IMMessageType.TEXT;
        }

        return imMessageType;
    }

    public static IMConversationType convertToIMConversationType(Message message){
        IMConversationType imConversationType = null;
        if(message.getConversationType().equals(Conversation.ConversationType.PRIVATE)){
            imConversationType = IMConversationType.PRIVATE;
        }
        return imConversationType;
    }

}
