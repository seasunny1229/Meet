package com.example.framework.backend.messaging.converter;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.message.IMMessageType;

import java.util.HashMap;

public abstract class IMInfoConverterManager<T> implements IMConverter<T>{

    private HashMap<IMConversationType, IMConversationInfoConverter<T>> conversationMap = new HashMap<>();

    private HashMap<IMMessageType, IMMessageInfoConverter<T>> messageMap = new HashMap<>();

    public IMInfoConverterManager() {
        addConversationConverters(conversationMap);
        addMessageConverters(messageMap);
    }

    protected abstract void addConversationConverters(HashMap<IMConversationType, IMConversationInfoConverter<T>> conversationMap);

    protected abstract void addMessageConverters(HashMap<IMMessageType, IMMessageInfoConverter<T>> messageMap);

    protected final IMConversationInfoConverter<T> getConversationConverter(IMConversationType imConversationType){
        if(!conversationMap.containsKey(imConversationType)){
            throw new IllegalArgumentException("The type is not registered in conversation converter map");
        }

        return conversationMap.get(imConversationType);
    }

    protected final IMMessageInfoConverter<T> getMessageConverter(IMMessageType imMessageType){
        if(!messageMap.containsKey(imMessageType)){
            throw new IllegalArgumentException("The type is not registered in message converter map");
        }
        return messageMap.get(imMessageType);
    }
}
