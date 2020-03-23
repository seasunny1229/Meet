package com.example.framework.backend.messaging.info;

import java.io.Serializable;

public class ExtraInfo implements Serializable {

    private String handlerClassName;

    private String conversationTypeName;

    private String messageTypeName;

    public ExtraInfo(String handlerClassName, String conversationTypeName, String messageTypeName) {
        this.handlerClassName = handlerClassName;
        this.conversationTypeName = conversationTypeName;
        this.messageTypeName = messageTypeName;
    }

    public ExtraInfo() {
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public void setHandlerClassName(String handlerClassName) {
        this.handlerClassName = handlerClassName;
    }

    public String getConversationTypeName() {
        return conversationTypeName;
    }

    public void setConversationTypeName(String conversationTypeName) {
        this.conversationTypeName = conversationTypeName;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }
}
