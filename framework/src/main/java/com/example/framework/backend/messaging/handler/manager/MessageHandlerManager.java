package com.example.framework.backend.messaging.handler.manager;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.handler.base.IMMessageReceivingHandler;
import com.example.framework.cloud.rongcloud.converter.filter.IMMessageFilter;

import java.util.HashMap;

public class MessageHandlerManager {

    public enum MessageHandlerFilterType{
        chat
    }


    private static MessageHandlerManager sInstance;

    public static MessageHandlerManager getInstance(){
        if(sInstance == null){
            sInstance = new MessageHandlerManager();
        }
        return sInstance;
    }

    private HashMap<Class<? extends BaseIMMessageReceivingHandler>, BaseIMMessageReceivingHandler> handleMap = new HashMap<>();

    private HashMap<MessageHandlerFilterType, IMMessageFilter> handlerFilterMap = new HashMap<>();

    public void init(BaseIMMessageReceivingHandler[] handlers){
        for(BaseIMMessageReceivingHandler handler : handlers){
            addHandler(handler);
        }
    }

    public void addHandler(BaseIMMessageReceivingHandler handler){
        if(handleMap.containsKey(handler.getClass())){
            return;
        }

        handleMap.put(handler.getClass(), handler);
    }

    public BaseIMMessageReceivingHandler getHandler(Class<? extends BaseIMMessageReceivingHandler> clazz){
        if(!handleMap.containsKey(clazz)){
            return null;
        }
        return handleMap.get(clazz);
    }

    public void addHandlerFilter(MessageHandlerFilterType type, IMMessageFilter imMessageFilter){
        if(!handlerFilterMap.containsKey(type)){
            handlerFilterMap.put(type, imMessageFilter);
        }
    }

    public IMMessageFilter getHandlerFilter(MessageHandlerFilterType type){
        if(!handlerFilterMap.containsKey(type)){
            throw new IllegalArgumentException("the argument is wrong because it can not be found in handler filter map key set");
        }
        return handlerFilterMap.get(type);
    }
}
