package com.example.framework.backend.messaging.message;


import com.example.framework.backend.messaging.conversation.IMConversationType;
import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;

public class IMMessage {


    // source id
    private String sourceId;

    // target id
    private String targetId;

    // handler
    private BaseIMMessageReceivingHandler messageReceivingHandler;

    // conversation type
    private IMConversationType imConversationType = IMConversationType.PRIVATE;

    // message type
    private IMMessageType imMessageType;

    // message time stamp;
    private long receivedTime;

    // extra info
    private String extra;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public BaseIMMessageReceivingHandler getMessageReceivingHandler() {
        return messageReceivingHandler;
    }

    public void setMessageReceivingHandler(BaseIMMessageReceivingHandler messageReceivingHandler) {
        this.messageReceivingHandler = messageReceivingHandler;
    }

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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", messageReceivingHandler=" + messageReceivingHandler +
                ", imConversationType=" + imConversationType +
                ", imMessageType=" + imMessageType +
                ", receivedTime=" + receivedTime +
                ", extra='" + extra + '\'' +
                '}';
    }
}
