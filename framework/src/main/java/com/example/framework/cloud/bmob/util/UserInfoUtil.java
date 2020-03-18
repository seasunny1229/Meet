package com.example.framework.cloud.bmob.util;

import com.example.framework.backend.bean.User;
import com.example.framework.cloud.bmob.bean.IMBmobUser;

public class UserInfoUtil {

    public static void updateUserInfoFromBackendData(User user, IMBmobUser imBmobUser){

        // unique ID
        user.setUid(imBmobUser.getObjectId());

        // 基本属性
        user.setNickName(imBmobUser.getNickName());
        user.setPhoto(imBmobUser.getPhoto());
        user.setMobilePhoneNumber(imBmobUser.getMobilePhoneNumber());
        user.setEmail(imBmobUser.getEmail());

        // 其他属性
        user.setSex(imBmobUser.isSex());
        user.setAge(imBmobUser.getAge());
        user.setBirthday(imBmobUser.getBirthday());
        user.setConstellation(imBmobUser.getConstellation());
        user.setHobby(imBmobUser.getHobby());
        user.setStatus(imBmobUser.getStatus());
        user.setDesc(imBmobUser.getDesc());
    };



}
