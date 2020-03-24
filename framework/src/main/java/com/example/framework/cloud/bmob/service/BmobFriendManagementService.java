package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.bean.Friend;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.cloud.Exception.BmobExceptionHandler;
import com.example.framework.cloud.bmob.bean.IMFriend;
import com.example.framework.exception.ExceptionFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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

    @Override
    public void addFriend(final String meId, final String friendId, final BackendServiceCallback<Void> backendServiceCallback) {

        // 查询好友列表，避免重复添加
        isFriend(meId, friendId, new BackendServiceCallback<Boolean>() {
            @Override
            public void success(Boolean aBoolean) {

                // 如果不在好友列表中，则添加好友
                if(!aBoolean){
                    IMFriend imFriend = new IMFriend(meId, friendId);
                    imFriend.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                backendServiceCallback.success(null);
                            }
                            else {
                                BmobExceptionHandler.handleBmobException(e);
                                backendServiceCallback.fail(ExceptionFactory.createNotifyUserBackendServiceException(e));
                            }
                        }
                    });
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                BmobExceptionHandler.handleBmobException(e);
                backendServiceCallback.fail(ExceptionFactory.createNotifyUserBackendServiceException(e));
            }
        });
    }


    @Override
    public void getAllFriends(String meId, final BackendServiceCallback<List<Friend>> backendServiceCallback) {
        BmobQuery<IMFriend> query = new BmobQuery<>();
        query.addWhereEqualTo("me", meId);
        query.findObjects(new FindListener<IMFriend>() {
            @Override
            public void done(List<IMFriend> list, BmobException e) {
                if(e == null){
                    List<Friend> friends = new ArrayList<>();
                    if(list != null){
                        for(IMFriend imFriend : list){
                            friends.add(new Friend(imFriend.getFriend()));
                        }
                    }
                    backendServiceCallback.success(friends);
                }
                else {
                    BmobExceptionHandler.handleBmobException(e);
                    backendServiceCallback.fail(ExceptionFactory.createNotifyUserBackendServiceException(e));
                }
            }
        });
    }
}
