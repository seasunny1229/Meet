package com.example.framework.backend.messaging.message;

import com.example.framework.backend.messaging.conversation.IMConversationType;

public abstract class IMMessage {

    // conversation type
    protected IMConversationType conversationType;

    // message type
    protected IMMessageType messageType;

    // target id
    protected String targetId;

    public IMMessageType getMessageType() {
        return messageType;
    }

    public IMConversationType getConversationType() {
        return conversationType;
    }

    public String getTargetId() {
        return targetId;
    }
}
