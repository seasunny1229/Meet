package com.example.framework.backend.service;

import android.content.Context;

import com.example.framework.backend.callback.BackendServiceCallback;

public interface IUserConnectionService {

    // 获取token（用户令牌）
    void getUserToken(Context context, BackendServiceCallback<String> backendServiceCallback);

    // 连接IM云服务器
    void connect(String token, BackendServiceCallback<String> backendServiceCallback);


}
