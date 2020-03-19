package com.example.meet.persistent.litepal.bean;

import com.example.meet.persistent.litepal.constant.LitePalConstant;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class NewFriend extends LitePalSupport {

    //留言
    private String message;

    //对方id
    private String uid;

    //时间
    private long time;

    //状态
    @Column(defaultValue = LitePalConstant.NEW_FRIEND_STATUS_DEFAULT_VALUE_TO_BE_CONFIRMED)
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
