package com.example.framework.backend.messaging.message;

import com.example.framework.backend.messaging.conversation.IMConversationType;

public class IMTextMessage extends IMMessage {

    private String messageContent;

    public static IMTextMessage createPrivateTextMessage(String targetId, String messageContent){
        IMTextMessage imTextMessage = new IMTextMessage();
        imTextMessage.conversationType = IMConversationType.PRIVATE;
        imTextMessage.messageType = IMMessageType.TEXT;
        imTextMessage.targetId = targetId;
        imTextMessage.messageContent = messageContent;
        return imTextMessage;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
