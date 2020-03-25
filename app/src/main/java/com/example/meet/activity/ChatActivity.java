package com.example.meet.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMMessageType;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.exception.ExceptionHandler;
import com.example.meet.R;
import com.example.meet.view.chat.ChatRecyclerViewAdapter;

import java.util.Collections;
import java.util.List;

public class ChatActivity extends BaseCommonStyleActivity {

    // intent
    public static final String INTENT_EXTRA_FRIEND_USER_ID = "friend_user_id";
    public static final String INTENT_EXTRA_FRIEND_USER_NAME = "friend_user_name";
    public static final String INTENT_EXTRA_FRIEND_USER_PHOTO = "friend_user_photo";

    // constant
    private static final long TIME_DISPLAY_DURATION = 5 * 60 * 1000;

    private String friendId, friendName, friendPhoto;

    private RecyclerView recyclerView;

    private ChatRecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // friend info
        friendId = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_ID);
        friendName = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_NAME);
        friendPhoto = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_PHOTO);

        // title
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(friendName);
        }

        // recycler view
        recyclerView = findViewById(R.id.mChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(recyclerViewAdapter = new ChatRecyclerViewAdapter(friendName, friendPhoto, UserManager.getInstance().getUser().getNickName(), UserManager.getInstance().getUser().getPhoto()));

        // update message data
        updateMessageData();
    }

    private  void updateMessageData(){
        IMessageService messageService = getBackendService(IMessageService.class);
        messageService.getLocalPrivateHistoryMessages(friendId, new BackendServiceCallback<List<IMMessage>>() {
            @Override
            public void success(List<IMMessage> imMessages) {

                // sort messages according to its time stamp from oldest to latest
                Collections.reverse(imMessages);

                // refresh adapter
                recyclerViewAdapter.clear();
                long time = -1;
                for(IMMessage imMessage : imMessages){
                    recyclerViewAdapter.add(imMessage);

                    // record time
                    if(time == -1){
                        time = imMessage.getReceivedTime();
                    }
                    else if(imMessage.getReceivedTime() - time > TIME_DISPLAY_DURATION){
                        IMMessage timeMessage = new IMMessage();
                        timeMessage.setImMessageType(IMMessageType.TIME);
                        timeMessage.setReceivedTime(imMessage.getReceivedTime());
                        recyclerViewAdapter.add(timeMessage);
                        time = imMessage.getReceivedTime();
                    }
                }
                recyclerView.scrollToPosition(recyclerViewAdapter.size() - 1);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(ChatActivity.this, e);
            }
        });
    }






}
