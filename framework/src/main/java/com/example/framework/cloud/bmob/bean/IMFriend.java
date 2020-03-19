package com.example.framework.cloud.bmob.bean;

import cn.bmob.v3.BmobObject;

public class IMFriend extends BmobObject {

    private String me;

    private String friend;

    // 星标朋友，黑名单等
    private String status;

    public IMFriend(String me, String friend) {
        this.me = me;
        this.friend = friend;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
