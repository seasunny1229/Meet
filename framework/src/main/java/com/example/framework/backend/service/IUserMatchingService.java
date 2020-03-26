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


}




