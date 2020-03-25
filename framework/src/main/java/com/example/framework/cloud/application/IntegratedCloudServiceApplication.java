package com.example.framework.cloud.application;

import com.example.framework.backend.application.BackendServiceApplication;
import com.example.framework.backend.service.IConversationService;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.backend.service.INewFriendManagementService;
import com.example.framework.backend.service.IConnectionService;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.backend.service.IUserLoginService;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.cloud.appserver.RongCloudAppServerManager;
import com.example.framework.cloud.bmob.service.BmobFriendManagementService;
import com.example.framework.cloud.bmob.service.BmobUserInfoService;
import com.example.framework.cloud.bmob.service.BmobUserLoginService;
import com.example.framework.cloud.bmob.service.BmobUserQueryService;
import com.example.framework.cloud.litepal.LitePalNewFriendManagementService;
import com.example.framework.cloud.rongcloud.service.RongCloudConnectionService;
import com.example.framework.cloud.rongcloud.service.RongCloudConversationService;
import com.example.framework.cloud.rongcloud.service.RongCloudMessageService;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import io.rong.imlib.RongIMClient;

/**
*
* 用Bmob集成云环境管理用户数据
*
*
*/

public abstract class IntegratedCloudServiceApplication extends BackendServiceApplication {

    // region service
    @Override
    protected void initBackendServiceModule() {
        initBmob();
        initRongCloud();
        initLitePal();

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBackendService(Class<T> clazz) {
        if(clazz == IUserLoginService.class){
            return (T) BmobUserLoginService.getInstance();
        }
        else if(clazz == IUserInfoService.class){
            return (T) BmobUserInfoService.getInstance();
        }
        else if(clazz == IUserQueryService.class){
            return (T) BmobUserQueryService.getInstance();
        }
        else if(clazz == IConnectionService.class){
            return (T) RongCloudConnectionService.getInstance(this);
        }
        else if(clazz == INewFriendManagementService.class){
            return (T) LitePalNewFriendManagementService.getInstance();
        }
        else if(clazz == IFriendManagementService.class){
            return (T) BmobFriendManagementService.getInstance();
        }
        else if(clazz == IMessageService.class){
            return (T) RongCloudMessageService.getInstance();
        }
        else if(clazz == IConversationService.class){
            return (T) RongCloudConversationService.getInstance();
        }

        return null;
    }

    // endregion

    // region initialize module
    private void initBmob(){
        // 初始化bmob
        Bmob.initialize(this, getBmobApplicationId());

        // 短信验证码功能
        Bmob.resetDomain("http://open-vip.bmob.cn/8/");
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {

            }
        });

    }

    private void initRongCloud(){

        // 融云 server
        RongCloudAppServerManager.getInstance().init(getRongCloudAppKey(), getRongCloudAppSecret());

        // IMLib
        RongIMClient.init(this, getRongCloudAppKey());

    }

    private void initLitePal(){
        LitePal.initialize(this);
    }
    // endregion

    // region config
    protected abstract String getAppId();

    protected abstract String getBmobApplicationId();

    protected abstract String getRongCloudAppKey();

    protected abstract String getRongCloudAppSecret();
    // endregion



}
