package com.example.meet.view.userinfo;

public class UserItemInfo {

    private int backgroundColor;

    private String type;

    private String value;

    public UserItemInfo(int backgroundColor, String type, String value) {
        this.backgroundColor = backgroundColor;
        this.type = type;
        this.value = value;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
