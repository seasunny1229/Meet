package com.example.meet.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.conversation.IMConversation;
import com.example.framework.backend.service.IConversationService;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.fragment.BaseFragment;
import com.example.meet.R;
import com.example.meet.eventbus.EventBusConstant;
import com.example.meet.view.chathistory.ChatHistoryRecyclerViewAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ChatHistoryFragment extends BaseFragment {


    private ChatHistoryRecyclerViewAdapter recyclerViewAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_history;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // event bus
        EventBusUtil.registerEventBus(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        this.view = view;

        // refresh layout
        swipeRefreshLayout = view.findViewById(R.id.mChatRecordRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateConversationData();
            }
        });

        // recycler view
        RecyclerView recyclerView = view.findViewById(R.id.mChatRecordView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter = new ChatHistoryRecyclerViewAdapter());

        // update data
        updateConversationData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregisterEventBus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        if(messageEvent.getType() == EventBusConstant.UPDATE_CHAT_INFO || messageEvent.getType() == EventBusConstant.CLEAR_UNREAD_MESSAGES){
            updateConversationData();
        }
    }

    private void updateConversationData(){
        swipeRefreshLayout.setRefreshing(true);
        final BaseActivity activity = (BaseActivity) view.getContext();
        IConversationService conversationService = activity.getBackendService(IConversationService.class);
        conversationService.getConversationList(new BackendServiceCallback<List<IMConversation>>() {
            @Override
            public void success(List<IMConversation> imConversations) {
                swipeRefreshLayout.setRefreshing(false);
                if(imConversations.isEmpty()){
                    updateUIWithEmptyList();
                }
                else {
                    updateUIWithNotEmptyList(imConversations);
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                swipeRefreshLayout.setRefreshing(false);
                ExceptionHandler.handleBackendServiceException(activity,e);
            }
        });
    }

    private void updateUIWithEmptyList(){
        view.findViewById(R.id.item_empty_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.mChatRecordView).setVisibility(View.GONE);
    }

    private void updateUIWithNotEmptyList(List<IMConversation> conversations){
        view.findViewById(R.id.item_empty_view).setVisibility(View.GONE);
        view.findViewById(R.id.mChatRecordView).setVisibility(View.VISIBLE);
        recyclerViewAdapter.clear();
        for(IMConversation imConversation : conversations){
            recyclerViewAdapter.add(imConversation);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }


}
