package com.example.framework.backend.bean;

public class Friend {

    private String id;

    private Object extra;

    public Friend(String id, Object extra) {
        this.id = id;
        this.extra = extra;
    }

    public Friend(String id) {
        this.id = id;
    }

    public Friend() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", extra=" + extra +
                '}';
    }
}
