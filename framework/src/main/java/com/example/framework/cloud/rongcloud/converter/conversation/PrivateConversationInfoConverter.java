package com.example.framework.cloud.rongcloud.converter.conversation;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.converter.IMConversationInfoConverter;
import com.example.framework.backend.messaging.message.IMMessage;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


public class PrivateConversationInfoConverter implements IMConversationInfoConverter<Message> {

    @Override
    public void convertToRemoteConversationInfo(IMMessage imMessage, Message message) {
        message.setConversationType(Conversation.ConversationType.PRIVATE);
    }

    @Override
    public void convertToLocalConversationInfo(IMMessage imMessage, Message message) {
        imMessage.setImConversationType(IMConversationType.PRIVATE);
    }
}
