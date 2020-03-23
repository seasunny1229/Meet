package com.example.framework.backend.messaging.message;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.handler.manager.MessageHandlerManager;

public class IMTextMessage extends IMMessage {

    private String messageContent;

    public static IMTextMessage createPrivateTextMessage(String targetId, String messageContent, Class<? extends BaseIMMessageReceivingHandler> clazz){
        IMTextMessage imTextMessage = new IMTextMessage();
        imTextMessage.messageContent = messageContent;
        imTextMessage.setTargetId(targetId);
        imTextMessage.setImMessageType(IMMessageType.TEXT);
        imTextMessage.setMessageReceivingHandler(MessageHandlerManager.getInstance().get(clazz));
        return imTextMessage;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public String toString() {
        return "IMTextMessage{" +
                "messageContent='" + messageContent + '\'' +
                '}';
    }
}
