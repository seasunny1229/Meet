package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.framework.backend.pushing.constant.PushType;
import com.example.framework.backend.service.IPushService;
import com.example.framework.cloud.Exception.BmobExceptionHandler;
import com.example.framework.cloud.bmob.bean.IMPush;
import com.example.framework.cloud.bmob.util.PushInfoUtil;
import com.example.framework.exception.ExceptionFactory;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class BmobPushService implements IPushService {
    private static final int DEFAULT_NUMBER_FETCH_PUSH_DATA_UPPER_LIMIT = 500;

    private static BmobPushService sInstance;

    public static BmobPushService getInstance(){
        if(sInstance == null){
            sInstance = new BmobPushService();
        }
        return sInstance;
    }

    @Override
    public void push(PushType type, String text, String mediaUrl, final BackendServiceCallback<Void> backendServiceCallback) {
        IMPush imPush = new IMPush();
        imPush.setType(type.toString());
        imPush.setUid(UserManager.getInstance().getUser().getUid());
        imPush.setTime(System.currentTimeMillis());
        imPush.setText(text);
        imPush.setMediaUrl(mediaUrl);
        imPush.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    backendServiceCallback.success(null);
                }
                else {
                    BmobExceptionHandler.handleBmobException(e);
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(e));
                }
            }
        });
    }

    @Override
    public void fetchPushedData(final BackendServiceCallback<List<PushInfo>> backendServiceCallback) {
        BmobQuery<IMPush> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(DEFAULT_NUMBER_FETCH_PUSH_DATA_UPPER_LIMIT);
        bmobQuery.findObjects(new FindListener<IMPush>() {
            @Override
            public void done(List<IMPush> list, BmobException e) {
                List<PushInfo> results = new ArrayList<>();
                if(e == null){
                    if(list != null && !list.isEmpty()){
                        for(IMPush imPush : list){
                            results.add(PushInfoUtil.covertToLocalData(imPush));
                        }
                    }
                    backendServiceCallback.success(results);
                }
                else {
                    BmobExceptionHandler.handleBmobException(e);
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(e));
                }
            }
        });
    }
}
