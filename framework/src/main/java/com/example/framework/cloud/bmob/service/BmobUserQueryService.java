package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.bmob.util.UserInfoUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobUserQueryService implements IUserQueryService<User> {

    private static BmobUserQueryService sInstance;

    public static BmobUserQueryService getInstance(){
        if(sInstance == null){
            sInstance = new BmobUserQueryService();
        }
        return sInstance;
    }


    @Override
    public void findUsersByPhoneNumber(String phoneNumber, final BackendServiceCallback<List<User>> backendServiceCallback) {
        BmobQuery<IMBmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("mobilePhoneNumber", phoneNumber);
        query.findObjects(new FindListener<IMBmobUser>() {
            @Override
            public void done(final List<IMBmobUser> list, BmobException e) {
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
                    backendServiceCallback.fail(new BackendServiceException(e));
                }
            }
        });
    }
}
