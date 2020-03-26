package com.example.framework.cloud.bmob.service;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.lifecycle.ServiceDisposable;
import com.example.framework.backend.service.IUserMatchingService;
import com.example.framework.cloud.Exception.BmobExceptionHandler;
import com.example.framework.cloud.bmob.bean.IMBmobUser;
import com.example.framework.cloud.bmob.bean.IMFate;
import com.example.framework.cloud.bmob.util.UserInfoUtil;
import com.example.framework.exception.ExceptionFactory;
import com.example.framework.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class BmobUserMatchingService implements IUserMatchingService, ServiceDisposable {

    private static BmobUserMatchingService sInstance;

    public static BmobUserMatchingService getInstance(){
        if(sInstance == null){
            sInstance = new BmobUserMatchingService();
        }
        return sInstance;
    }

    private Disposable disposable;

    private boolean isMatching, isLock;

    private IMFate imFate;

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

    @Override
    public void matchUserByRandom(final User user, final BackendServiceCallback<User> backendServiceCallback) {
        BmobQuery<IMBmobUser> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<IMBmobUser>() {
            @Override
            public void done(List<IMBmobUser> list, BmobException e) {
                Collections.shuffle(list);
                handleQueryResult(user, list, e, backendServiceCallback);
            }
        });
    }

    @Override
    public void matchUserBySoul(final User user, final BackendServiceCallback<User> backendServiceCallback) {
        BmobQuery<IMBmobUser> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("constellation", user.getConstellation());
        bmobQuery.addWhereEqualTo("hobby", user.getHobby());
        bmobQuery.findObjects(new FindListener<IMBmobUser>() {
            @Override
            public void done(List<IMBmobUser> list, BmobException e) {
                handleQueryResult(user, list, e, backendServiceCallback);
            }
        });
    }

    @Override
    public void matchUserByFate(final User user, final BackendServiceCallback<User> backendServiceCallback) {
        if(isMatching){
            return;
        }

        // flag
        isMatching = true;

        // put user on fate pool
        imFate = new IMFate();
        imFate.setId(user.getUid());
        imFate.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Observable.intervalRange(1,5,0,1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            LogUtil.e("查询开始");
                            isLock = false;
                            disposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            LogUtil.e("查询");

                            // query IM Fate pool
                            BmobQuery<IMFate> bmobQuery = new BmobQuery<>();
                            bmobQuery.addWhereNotEqualTo("id", user.getUid());
                            bmobQuery.findObjects(new FindListener<IMFate>() {
                                @Override
                                public void done(List<IMFate> list, BmobException e) {
                                    if(e == null && !list.isEmpty()){

                                        // query user by id
                                        BmobUserQueryService.getInstance().findUsersById(list.get(0).getId(), new BackendServiceCallback<List<User>>() {
                                            @Override
                                            public void success(List<User> users) {

                                                // dispose to release resources
                                                dispose();

                                                // callback
                                                backendServiceCallback.success(users.get(0));
                                            }

                                            @Override
                                            public void fail(BackendServiceException e) {
                                                BmobExceptionHandler.handleBmobException(e);
                                            }
                                        });
                                    }
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("查询失败");
                            dispose();
                            backendServiceCallback.success(null);
                        }

                        @Override
                        public void onComplete() {
                            LogUtil.e("查询结束");
                            dispose();
                            backendServiceCallback.success(null);
                        }
                    });
                }
                else {
                    isMatching = false;
                    BmobExceptionHandler.handleBmobException(e);
                    backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(e));
                }
            }
        });
    }

    @Override
    public void matchUserByLove(final User user, final BackendServiceCallback<User> backendServiceCallback) {
        BmobQuery<IMBmobUser> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("sex", !user.isSex());
        bmobQuery.addWhereGreaterThan("age", user.getAge() - 3);
        bmobQuery.addWhereLessThan("age", user.getAge() + 3);
        bmobQuery.findObjects(new FindListener<IMBmobUser>() {
            @Override
            public void done(List<IMBmobUser> list, BmobException e) {
                handleQueryResult(user, list, e, backendServiceCallback);
            }
        });
    }

    @Override
    public void dispose() {
        if(isLock){
            return;
        }

        isLock = true;
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        // delete from IM Fate pool
        imFate.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    BmobExceptionHandler.handleBmobException(e);
                }
                isMatching = false;
            }
        });
    }

    private void handleQueryResult(User myUser, List<IMBmobUser> list, BmobException e, BackendServiceCallback<User> backendServiceCallback){
        if(e == null){
            if(list != null && !list.isEmpty() && (list.size() != 1 || !list.get(0).getObjectId().equals(myUser.getUid()))){
                User user = new User();
                Collections.shuffle(list);
                if(list.get(0).getObjectId().equals(myUser.getUid())){
                    UserInfoUtil.updateLocalUserInfoFromRemoteData(user, list.get(1));
                }
                else {
                    UserInfoUtil.updateLocalUserInfoFromRemoteData(user, list.get(0));
                }
                backendServiceCallback.success(user);
            }
            else {
                backendServiceCallback.success(null);
            }
        }
        else {
            BmobExceptionHandler.handleBmobException(e);
            backendServiceCallback.fail(ExceptionFactory.createNotNotifyUserBackendServiceException(e));
        }
    }



}
