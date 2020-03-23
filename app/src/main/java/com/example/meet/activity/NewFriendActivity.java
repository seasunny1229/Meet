package com.example.meet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.INewFriendManagementService;
import com.example.framework.exception.ExceptionHandler;
import com.example.meet.R;
import com.example.meet.persistent.litepal.bean.NewFriend;
import com.example.meet.view.newfriend.NewFriendRecyclerViewAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * 新朋友管理
 *
 *
 */

public class NewFriendActivity extends BaseFullScreenStyleActivity {

    private Disposable disposable;

    private NewFriendRecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        // init view
        initView();

        // show new friend data
        updateNewFriendList();
    }

    private void initView(){
        RecyclerView recyclerView = findViewById(R.id.mNewFriendView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter = new NewFriendRecyclerViewAdapter());
    }


    private void updateNewFriendList(){
        Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<NewFriend>> emitter) throws Exception {

                // find all new friends
                INewFriendManagementService service = getBackendService(INewFriendManagementService.class);
                service.queryAllNewFriends(NewFriend.class, new BackendServiceCallback<List<NewFriend>>() {
                    @Override
                    public void success(List<NewFriend> newFriends) {
                        emitter.onNext(newFriends);
                    }

                    @Override
                    public void fail(BackendServiceException e) {
                        ExceptionHandler.handleBackendServiceException(NewFriendActivity.this, e);
                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewFriend>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<NewFriend> newFriends) {

                        // have no new friends
                        if(newFriends.isEmpty()){
                            showEmptyFriends();
                        }

                        // have one or more new friends
                        else {

                            // sort friend according to time received
                            Collections.sort(newFriends, new Comparator<NewFriend>() {
                                @Override
                                public int compare(NewFriend o1, NewFriend o2) {
                                    return (int) (o1.getTime() - o2.getTime());
                                }
                            });

                            // show new friends
                            showNewFriends(newFriends);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showEmptyFriends(){
        findViewById(R.id.item_empty_view).setVisibility(View.VISIBLE);
    }

    private void showNewFriends(List<NewFriend> newFriends){
        recyclerViewAdapter.clear();
        for(NewFriend newFriend : newFriends){
            recyclerViewAdapter.add(newFriend);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
