package com.example.framework.cloud.litepal;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.service.INewFriendManagementService;
import com.example.framework.exception.ExceptionFactory;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class LitePalNewFriendManagementService implements INewFriendManagementService {

    private static LitePalNewFriendManagementService sInstance;

    public static LitePalNewFriendManagementService getInstance(){
        if(sInstance == null){
            sInstance = new LitePalNewFriendManagementService();
        }
        return sInstance;
    }


    @Override
    public <T> void addNewFriend(T t, BackendServiceCallback<T> backendServiceCallback) {
        boolean isSuccessful = ((LitePalSupport)t).save();
        if(isSuccessful){
            backendServiceCallback.success(t);
        }
        else {
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
        }
    }

    @Override
    public <T> void updateNewFriendStatus(T t, BackendServiceCallback<T> backendServiceCallback) {
        boolean isSuccessful = ((LitePalSupport)t).save();
        if(isSuccessful){
            backendServiceCallback.success(t);
        }
        else {
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
        }
    }

    @Override
    public <T> void queryAllNewFriends(Class<T> clazz, final BackendServiceCallback<List<T>> backendServiceCallback) {
        LitePal.findAllAsync(clazz).listen(new FindMultiCallback<T>() {
            @Override
            public void onFinish(List<T> list) {
                backendServiceCallback.success(list);
            }
        });
    }
}
