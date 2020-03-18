package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.marker.IUser;

import java.util.List;

public interface IUserQueryService<T extends IUser> {

    // 通过手机号码查询匹配用户
    void findUsersByPhoneNumber(String phoneNumber, BackendServiceCallback<List<T>> backendServiceCallback);


}
