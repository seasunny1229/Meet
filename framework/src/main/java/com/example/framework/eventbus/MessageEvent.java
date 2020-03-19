package com.example.framework.eventbus;

import androidx.annotation.NonNull;


public class MessageEvent {

    public static MessageEvent newMessage(int type){
        return new MessageEvent(type);
    }

    private int type;

    private Object data;

    public MessageEvent() {
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    @NonNull
    public String toString() {
        return "MessageEvent{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }


}
