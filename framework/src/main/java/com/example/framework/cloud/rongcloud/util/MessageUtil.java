package com.example.framework.cloud.rongcloud.util;

import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class MessageUtil {

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
        message.setContent(TextMessage.obtain(messageContent));
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

}
