package com.example.framework.cloud.rongcloud.util;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class RongCloudMessageConvertor {

    public static Message convertToRongCloudMessage(IMMessage imMessage){

        Message message = new Message();
        message.setTargetId(imMessage.getTargetId());
        message.setConversationType(getConversationType(imMessage.getConversationType()));
        switch (imMessage.getMessageType()){
            case TEXT:
                createTextMessage(message, imMessage);
                break;
            default:
                break;
        }
        return message;
    }

    private static void createTextMessage(Message message, IMMessage imMessage){
        String messageContent = ((IMTextMessage)imMessage).getMessageContent();
        String extra = imMessage.getExtra();
        TextMessage textMessage = TextMessage.obtain(messageContent);
        textMessage.setExtra(extra);
        message.setContent(textMessage);
    }

    private static Conversation.ConversationType getConversationType(IMConversationType type){
        Conversation.ConversationType result = null;
        switch (type){
            case PRIVATE:
                result = Conversation.ConversationType.PRIVATE;
                break;
            default:
                break;
        }
        return result;
    }

    public static IMMessage convertToIMMessage(Message message){
        return null;
    }

}
