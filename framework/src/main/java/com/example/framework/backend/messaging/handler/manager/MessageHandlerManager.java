package com.example.framework.backend.messaging.handler.manager;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;

import java.util.HashMap;

public class MessageHandlerManager {
    private static MessageHandlerManager sInstance;

    public static MessageHandlerManager getInstance(){
        if(sInstance == null){
            sInstance = new MessageHandlerManager();
        }
        return sInstance;
    }

    public void init(BaseIMMessageReceivingHandler[] handlers){
        for(BaseIMMessageReceivingHandler handler : handlers){
            add(handler);
        }
    }

    private HashMap<Class<? extends BaseIMMessageReceivingHandler>, BaseIMMessageReceivingHandler> handleMap = new HashMap<>();

    public void add(BaseIMMessageReceivingHandler handler){
        if(handleMap.containsKey(handler.getClass())){
            return;
        }

        handleMap.put(handler.getClass(), handler);
    }

    public BaseIMMessageReceivingHandler get(Class<? extends BaseIMMessageReceivingHandler> clazz){
        if(!handleMap.containsKey(clazz)){
            throw new IllegalArgumentException("cannot find handler object");
        }
        return handleMap.get(clazz);
    }

}
