package com.example.framework.backend.messaging.handler.manager;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.handler.friend.AddingFriendAgreeHandler;
import com.example.framework.backend.messaging.handler.friend.AddingFriendRequestHandler;

import java.util.HashMap;

public class MessageHandlerManager {
    private static MessageHandlerManager sInstance;

    public static MessageHandlerManager getInstance(){
        if(sInstance == null){
            sInstance = new MessageHandlerManager();
        }
        return sInstance;
    }

    private MessageHandlerManager(){
        add(new AddingFriendRequestHandler());
        add(new AddingFriendAgreeHandler());
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
