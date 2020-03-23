package com.example.framework.backend.messaging.handler.friend;

import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.message.IMMessageType;

public abstract class BaseFriendManagementHandler extends BaseIMMessageReceivingHandler {

    public BaseFriendManagementHandler() {
        setImConversationType(IMConversationType.PRIVATE);
        setImMessageType(IMMessageType.TEXT);
    }
}
