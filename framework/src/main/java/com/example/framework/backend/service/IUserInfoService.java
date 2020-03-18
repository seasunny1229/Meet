package com.example.framework.backend.service;


import android.app.Application;
import android.content.Context;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.marker.IUser;

import java.io.File;

/**
 *
 * 登录以后，在主页使用
 *
 */

public interface IUserInfoService<T extends IUser> {

    // 判断当前用户是否登录
    boolean isLogin();

    // 获取本地登录对象
    T getUser();

    // 更新昵称和上传头像
    void updateNicknameAndHeadPortrait(String nickName, File headPortraitFile, BackendServiceCallback<T> backendServiceCallback);

    // 获取token
    void getUserToken(Context context, BackendServiceCallback<String> backendServiceCallback);

}
