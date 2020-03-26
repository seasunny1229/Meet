package com.example.framework.backend.service;

/*
*
* 根据一定的规则，查找合适的用户
*/

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;

import java.util.List;

public interface IUserMatchingService {

    // 随机查找一定数量的用户
    void randomFindUsers(int count, BackendServiceCallback<List<User>> backendServiceCallback);

    // 随机匹配
    void matchUserByRandom(User user, BackendServiceCallback<User> backendServiceCallback);

    // 灵魂匹配
    void matchUserBySoul(User user, BackendServiceCallback<User> backendServiceCallback);

    // 缘分匹配
    void matchUserByFate(User user, BackendServiceCallback<User> backendServiceCallback);

    // 恋爱匹配
    void matchUserByLove(User user, BackendServiceCallback<User> backendServiceCallback);


}




