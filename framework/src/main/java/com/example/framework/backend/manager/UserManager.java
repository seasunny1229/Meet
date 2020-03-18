package com.example.framework.backend.manager;

import com.example.framework.backend.bean.User;

public class UserManager {

    private static UserManager sInstance;

    public static UserManager getInstance(){
        if(sInstance == null){
            sInstance = new UserManager();
        }
        return sInstance;
    }

    private User user;

    private UserManager(){
        user = new User();
    }

    public User getUser() {
        return user;
    }


}
