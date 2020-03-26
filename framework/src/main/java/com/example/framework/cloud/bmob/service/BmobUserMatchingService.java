package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.service.IUserMatchingService;
import com.example.framework.cloud.Exception.BmobExceptionHandler;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.bmob.util.UserInfoUtil;
import com.example.framework.exception.ExceptionFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobUserMatchingService implements IUserMatchingService {

    private static BmobUserMatchingService sInstance;

    public static BmobUserMatchingService getInstance(){
        if(sInstance == null){
            sInstance = new BmobUserMatchingService();
        }
        return sInstance;
    }

    @Override
    public void randomFindUsers(int count, final BackendServiceCallback<List<User>> backendServiceCallback) {
        BmobQuery<IMBmobUser> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(count);
        bmobQuery.findObjects(new FindListener<IMBmobUser>() {
            @Override
            public void done(List<IMBmobUser> list, BmobException e) {
                if(e == null){
                    List<User> users = new ArrayList<User>();
                    for(IMBmobUser imBmobUser : list){
                        User user = new User();
                        UserInfoUtil.updateLocalUserInfoFromRemoteData(user, imBmobUser);
                        users.add(user);
                    }
                    backendServiceCallback.success(users);
                }
                else {
                    BmobExceptionHandler.handleBmobException(e);
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(e));
                }
            }
        });

    }
}
