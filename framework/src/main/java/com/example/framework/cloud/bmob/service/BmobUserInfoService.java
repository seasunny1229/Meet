package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.bmob.util.UserInfoUtil;
import com.example.framework.exception.ExceptionFactory;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class BmobUserInfoService implements IUserInfoService<User> {

    private static BmobUserInfoService sInstance;

    public static BmobUserInfoService getInstance(){
        if(sInstance == null){
            sInstance = new BmobUserInfoService();
        }
        return sInstance;
    }

    @Override
    public boolean isLogin() {
        return BmobUser.isLogin();
    }

    @Override
    public User getUser() {
        IMBmobUser imBmobUser = BmobUser.getCurrentUser(IMBmobUser.class);
        User user = UserManager.getInstance().getUser();
        UserInfoUtil.updateLocalUserInfoFromRemoteData(user, imBmobUser);
        return user;
    }

    @Override
    public void updateNicknameAndHeadPortrait(final String nickName, File headPortraitFile, final BackendServiceCallback<User> backendServiceCallback) {

        // bmob file
        final BmobFile bmobFile = new BmobFile(headPortraitFile);

        // bmob user
        final IMBmobUser imBmobUser = BmobUser.getCurrentUser(IMBmobUser.class);

        // upload
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                // upload successful
                if(e == null){

                    // update imBmobUser
                    imBmobUser.setTokenNickName(nickName);
                    imBmobUser.setTokenPhoto(bmobFile.getFileUrl());
                    imBmobUser.setNickName(nickName);
                    imBmobUser.setPhoto(bmobFile.getFileUrl());

                    // upload imBmobUser profile
                    imBmobUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                            // update success
                            if(e == null){
                                backendServiceCallback.success(getUser());
                            }

                            // update fail
                            else {
                                backendServiceCallback.fail(new BackendServiceException(e));
                            }
                        }
                    });

                }

                // upload fail
                else {
                    backendServiceCallback.fail(new BackendServiceException(e));
                }


            }
        });

    }


    @Override
    public void updateUserInfo(User user, final BackendServiceCallback<User> backendServiceCallback) {
        IMBmobUser imBmobUser = BmobUser.getCurrentUser(IMBmobUser.class);
        UserInfoUtil.updateRemoteUserInfoFromLocalData(user, imBmobUser);
        imBmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    backendServiceCallback.success(null);
                }
                else {
                    backendServiceCallback.fail(ExceptionFactory.createNotifyUserBackendServiceException());
                }
            }
        });
    }
}
