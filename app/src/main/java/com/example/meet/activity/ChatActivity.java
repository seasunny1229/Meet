package com.example.meet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMMessageType;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.exception.ExceptionHandler;
import com.example.meet.R;
import com.example.meet.eventbus.EventBusConstant;
import com.example.meet.handler.chat.ChatHandler;
import com.example.meet.view.chat.ChatRecyclerViewAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(recyclerViewAdapter.size() - 1);
            }
        });

        // set button event listeners
        initButtons();

        // update message data
        updateHistoryMessageData();

        // register event bus
        EventBusUtil.registerEventBus(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregisterEventBus(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        if(messageEvent.getType() == EventBusConstant.UPDATE_CHAT_INFO){
            addIMMessageOnUI((IMMessage) messageEvent.getData());
        }
    }

    private void initButtons(){

        // send button
        Button sendButton = findViewById(R.id. btn_send_msg);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndUpdateText();
            }
        });


    }

    private void updateHistoryMessageData(){
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

                    // record and add time
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

                    // add IM Message
                    recyclerViewAdapter.add(imMessage);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(ChatActivity.this, e);
            }
        });
    }

    private void sendAndUpdateText(){

        // edit text
        EditText editText = findViewById(R.id.et_input_msg);
        String inputText = editText.getText().toString().trim();
        if(TextUtils.isEmpty(inputText)){
           return;
        }

        // send
        IMessageService messageService = getBackendService(IMessageService.class);
        IMTextMessage imTextMessage = IMTextMessage.createPrivateTextMessage(friendId, inputText, ChatHandler.class);
        messageService.sendMessage(imTextMessage, new BackendServiceCallback<Object>() {
            @Override
            public void success(Object o) {

            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(ChatActivity.this, e);
            }
        });

        // update local activity
        addIMMessageOnUI(imTextMessage);

        // clear edit text
        editText.getText().clear();
    }

    private void addIMMessageOnUI(IMMessage imMessage){
        recyclerViewAdapter.add(imMessage);
        recyclerViewAdapter.notifyDataSetChanged();
    }



}
