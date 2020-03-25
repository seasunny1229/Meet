package com.example.framework.cloud.rongcloud.converter.filter;

import com.example.framework.backend.messaging.message.IMMessage;

public class IMMessageReceiveHandlerFilter implements IMMessageFilter {

    private Class<?> clazz;

    public IMMessageReceiveHandlerFilter(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public boolean isValid(IMMessage imMessage) {
        return imMessage.getMessageReceivingHandler() != null && imMessage.getMessageReceivingHandler().getClass() == clazz;
    }
}



