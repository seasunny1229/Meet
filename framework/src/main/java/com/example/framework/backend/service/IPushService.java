package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.framework.backend.pushing.constant.PushType;

import java.util.List;

public interface IPushService {


    void push(PushType type, String text, String mediaUrl, BackendServiceCallback<Void> backendServiceCallback);


    void fetchPushedData(BackendServiceCallback<List<PushInfo>> backendServiceCallback);
}
