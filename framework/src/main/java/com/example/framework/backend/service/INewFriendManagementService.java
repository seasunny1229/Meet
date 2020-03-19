package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;

import java.util.List;

/**
 *
 * 维护新朋友列表数据
 *
 */

public interface INewFriendManagementService {

    // 添加新朋友
    <T> void addNewFriend(T t, BackendServiceCallback<T> backendServiceCallback);

    // 更新新朋友状态
    <T> void updateNewFriendStatus(T t, BackendServiceCallback<T> backendServiceCallback);

    // 查询所有新朋友
    <T> void queryAllNewFriends(Class<T> clazz, BackendServiceCallback<List<T>> backendServiceCallback);

}
