package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.cloud.bmob.bean.IMFriend;
import com.example.framework.exception.ExceptionFactory;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobFriendManagementService implements IFriendManagementService {

    private static BmobFriendManagementService sInstance;

    public static BmobFriendManagementService getInstance(){
        if(sInstance == null){
            sInstance = new BmobFriendManagementService();
        }
        return sInstance;
    }

    @Override
    public void isFriend(String meId, String friendId, final BackendServiceCallback<Boolean> backendServiceCallback) {
        BmobQuery<IMFriend> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("me", meId);
        bmobQuery.addWhereEqualTo("friend",friendId);
        bmobQuery.findObjects(new FindListener<IMFriend>() {
            @Override
            public void done(List<IMFriend> list, BmobException e) {
                if(e == null){
                    backendServiceCallback.success(!list.isEmpty());
                }
                else if(e.getErrorCode() == 101){
                    backendServiceCallback.success(false);
                }
                else {
                    backendServiceCallback.fail(ExceptionFactory.createNotifyUserBackendServiceException());
                }
            }
        });
    }
}
