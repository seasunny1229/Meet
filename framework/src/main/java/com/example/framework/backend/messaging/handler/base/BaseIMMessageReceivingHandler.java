package com.example.framework.backend.messaging.handler.base;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.message.IMMessageType;

public abstract class BaseIMMessageReceivingHandler implements IMMessageReceivingHandler {

    private IMConversationType imConversationType;

    private IMMessageType imMessageType;

    public IMConversationType getImConversationType() {
        return imConversationType;
    }

    public void setImConversationType(IMConversationType imConversationType) {
        this.imConversationType = imConversationType;
    }

    public IMMessageType getImMessageType() {
        return imMessageType;
    }

    public void setImMessageType(IMMessageType imMessageType) {
        this.imMessageType = imMessageType;
    }
}
