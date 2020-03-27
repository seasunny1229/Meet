package com.example.meet.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.framework.backend.service.IPushService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.fragment.BaseFragment;
import com.example.meet.R;
import com.example.meet.view.push.PushRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;

public class SquareFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_square;
    }

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;

    private PushRecyclerViewAdapter pushRecyclerViewAdapter;

    @Override
    protected void initView(final View view) {
        super.initView(view);
        this.view = view;

        // swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.mSquareSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

        // recycler view
        final RecyclerView recyclerView = view.findViewById(R.id.mSquareView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pushRecyclerViewAdapter = new PushRecyclerViewAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.getLayoutManager() != null){
                    if(((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 5){
                        view.findViewById(R.id.fb_squaue_top).setVisibility(View.VISIBLE);
                    }
                    else {
                        view.findViewById(R.id.fb_squaue_top).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // floating action button
        view.findViewById(R.id.fb_squaue_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private void updateData(){
        swipeRefreshLayout.setRefreshing(true);
        BaseActivity activity = (BaseActivity) view.getContext();
        IPushService pushService = activity.getBackendService(IPushService.class);
        pushService.fetchPushedData(new BackendServiceCallback<List<PushInfo>>() {
            @Override
            public void success(List<PushInfo> pushInfos) {
                swipeRefreshLayout.setRefreshing(false);
                if(pushInfos.isEmpty()){
                    showEmptyView();
                }
                else {
                    showNotEmptyView(pushInfos);
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(view.getContext(), e);
            }
        });
    }

    private void showEmptyView(){
        view.findViewById(R.id.item_empty_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.mSquareView).setVisibility(View.GONE);
    }

    private void showNotEmptyView(List<PushInfo> list){
        view.findViewById(R.id.item_empty_view).setVisibility(View.GONE);
        view.findViewById(R.id.mSquareView).setVisibility(View.VISIBLE);
        Collections.reverse(list);
        for(PushInfo pushInfo : list){
            pushRecyclerViewAdapter.add(pushInfo);
        }
        pushRecyclerViewAdapter.notifyDataSetChanged();
    }


}
