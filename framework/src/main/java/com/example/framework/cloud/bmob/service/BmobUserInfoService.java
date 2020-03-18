package com.example.framework.cloud.bmob.service;

import android.content.Context;

import com.example.framework.R;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.cloud.Exception.CloudExceptionHandler;
import com.example.framework.cloud.application.IntegratedCloudServiceApplication;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.bmob.util.UserInfoUtil;
import com.example.framework.cloud.rongcloud.constant.RongCloudResponseCode;
import com.example.framework.exception.ExceptionFactory;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.rong.RongCloud;
import io.rong.models.response.TokenResult;
import io.rong.models.user.UserModel;

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
        UserInfoUtil.updateUserInfoFromBackendData(user, imBmobUser);
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
    public void getUserToken(Context context, BackendServiceCallback<String> backendServiceCallback){
        IMBmobUser imBmobUser = BmobUser.getCurrentUser(IMBmobUser.class);
        UserModel userModel = new UserModel()
                .setId(imBmobUser.getObjectId())
                .setName(imBmobUser.getTokenNickName())
                .setPortrait(imBmobUser.getTokenPhoto());
        RongCloud rongCloud = ((IntegratedCloudServiceApplication)context.getApplicationContext()).getRongCloud();
        try {
            TokenResult result = rongCloud.user.register(userModel);
            if (result.getCode() == context.getResources().getInteger(R.integer.http_response_code_ok)) {
                backendServiceCallback.success(result.getToken());
            }
            else if(result.getCode() == RongCloudResponseCode.NUM_USERS_MEET_UPPER_LIMIT){
                CloudExceptionHandler.handlerRongCloudServerException(result);
                backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
            }
        }catch (Exception e){
            CloudExceptionHandler.handleRongCloudServerException(e);
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException());
        }
    }
}
