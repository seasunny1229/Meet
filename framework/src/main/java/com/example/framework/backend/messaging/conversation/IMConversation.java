package com.example.framework.backend.messaging.conversation;

import com.example.framework.backend.messaging.message.IMMessage;

public class IMConversation {

    private IMConversationType conversationType;

    private String targetId;

    private IMMessage latestIMMessage;

    private long receivedTime;

    private int unreadMessageCount;

    public IMConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(IMConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public IMMessage getLatestIMMessage() {
        return latestIMMessage;
    }

    public void setLatestIMMessage(IMMessage latestIMMessage) {
        this.latestIMMessage = latestIMMessage;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }
}
