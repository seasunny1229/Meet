package com.example.meet.view.addingfriend;

import com.example.framework.backend.marker.Info;


public class AddingFriendTitleInfo implements Info {

    private String title;

    public AddingFriendTitleInfo(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
