package com.example.framework.backend.pushing.bean;

import com.example.framework.backend.pushing.constant.PushType;

public class PushInfo {

    private PushType type;

    private String uid;

    private long time;

    private String text;

    private String mediaUrl;

    public PushType getType() {
        return type;
    }

    public void setType(PushType type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @Override
    public String toString() {
        return "PushInfo{" +
                "type=" + type +
                ", uid='" + uid + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                '}';
    }
}
