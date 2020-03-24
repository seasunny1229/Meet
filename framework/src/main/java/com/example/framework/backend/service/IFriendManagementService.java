package com.example.framework.backend.service;


import com.example.framework.backend.bean.Friend;
import com.example.framework.backend.callback.BackendServiceCallback;

import java.util.List;

/**
 *
 * 维护朋友列表数据
 *
 */

public interface IFriendManagementService {


    void isFriend(String meId, String friendId, BackendServiceCallback<Boolean> backendServiceCallback);

    void addFriend(String meId, String friendId, BackendServiceCallback<Void> backendServiceCallback);

    void getAllFriends(String meId, BackendServiceCallback<List<Friend>> backendServiceCallback);
}