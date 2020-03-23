package com.example.framework.cloud.rongcloud.converter.message;

import com.example.framework.backend.messaging.converter.IMMessageInfoConverter;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMMessageType;
import com.example.framework.backend.messaging.message.IMTextMessage;

import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class TextMessageInfoConverter implements IMMessageInfoConverter<Message> {

    @Override
    public void convertToRemoteMessageInfo(IMMessage imMessage, Message message) {
        String messageContent = ((IMTextMessage)imMessage).getMessageContent();
        TextMessage textMessage = TextMessage.obtain(messageContent);
        message.setContent(textMessage);
    }

    @Override
    public void convertToLocalMessageInfo(IMMessage imMessage, Message message) {
        TextMessage textMessage = (TextMessage) message.getContent();
        imMessage.setImMessageType(IMMessageType.TEXT);
        ((IMTextMessage) imMessage).setMessageContent(textMessage.getContent());
    }


}
