package com.example.meet.fragment;

import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.Friend;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.fragment.BaseFragment;
import com.example.meet.R;
import com.example.meet.view.allfriend.AllFriendRecyclerViewAdapter;

import java.util.List;

public class AllFriendFragment extends BaseFragment {

    private View view;

    private AllFriendRecyclerViewAdapter recyclerViewAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_friend;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        this.view = view;

        // refresh layout
        swipeRefreshLayout = view.findViewById(R.id.mAllFriendRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriendData();
            }
        });

        // recycler view
        RecyclerView recyclerView = view.findViewById(R.id.mAllFriendView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter = new AllFriendRecyclerViewAdapter());

        // update friend data
        updateFriendData();
    }

    private void updateFriendData(){
        swipeRefreshLayout.isRefreshing();
        final BaseActivity activity = (BaseActivity) view.getContext();
        IFriendManagementService service = activity.getBackendService(IFriendManagementService.class);
        service.getAllFriends(UserManager.getInstance().getUser().getUid(), new BackendServiceCallback<List<Friend>>() {
            @Override
            public void success(List<Friend> friends) {
                swipeRefreshLayout.setRefreshing(false);
                if(friends.isEmpty()){
                    updateUIWithEmptyFriendList();
                }
                else {
                    updateUIWithNotEmptyFriendList(friends);
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });
    }

    private void updateUIWithEmptyFriendList(){
        view.findViewById(R.id.item_empty_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.mAllFriendView).setVisibility(View.GONE);
    }

    private void updateUIWithNotEmptyFriendList(List<Friend> friends){
        view.findViewById(R.id.item_empty_view).setVisibility(View.GONE);
        view.findViewById(R.id.mAllFriendView).setVisibility(View.VISIBLE);
        recyclerViewAdapter.clear();
        for(Friend friend : friends){
            recyclerViewAdapter.add(friend);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }


}
