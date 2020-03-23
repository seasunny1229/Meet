package com.example.meet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.manager.UserManager;
import com.example.meet.handler.friend.AddingFriendRequestHandler;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IFriendManagementService;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.framework.view.custom.ModifiedDialog;
import com.example.meet.R;
import com.example.meet.view.userinfo.UserInfoRecyclerViewAdapter;
import com.example.meet.view.userinfo.UserItemInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 朋友信息
 * 对于没有添加的新朋友，显示添加新朋友按键
 * 对于已经添加的好友，显示聊天，语音，视频按键
 *
 */

public class UserInfoActivity extends BaseFullScreenStyleActivity {

    public static final String INTENT_EXTRA_FRIEND_USER_ID = "friend_user_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 更新用户信息
        updateUserInfo();

    }

    private void updateUserInfo(){
        IFriendManagementService friendManagementService = getBackendService(IFriendManagementService.class);
        friendManagementService.isFriend(UserManager.getInstance().getUser().getUid(), getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_ID), new BackendServiceCallback<Boolean>() {
            @Override
            public void success(Boolean aBoolean) {
                updateUserInfo(aBoolean);
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(UserInfoActivity.this, e);
            }
        });
    }

    private void updateUserInfo(final boolean isFriend){

        final String userId = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_ID);

        IUserQueryService<User> useQueryService = getBackendService(IUserQueryService.class);
        useQueryService.findUsersById(userId, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                updateUserInfo(users.get(0));
                configButtons(isFriend);
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(UserInfoActivity.this, e);
            }
        });
    }

    private void updateUserInfo(User user){

        // name
        TextView nicknameView = findViewById(R.id.tv_nickname);
        nicknameView.setText(user.getNickName());

        // portrait
        CircleImageView portraitView = findViewById(R.id.iv_user_photo);
        GlideUtil.load(UserInfoActivity.this, user.getPhoto(), 100,  100, portraitView);

        // recycler view for other user infos
        RecyclerView recyclerView = findViewById(R.id.mUserInfoView);
        UserInfoRecyclerViewAdapter userInfoRecyclerViewAdapter;
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(userInfoRecyclerViewAdapter = new UserInfoRecyclerViewAdapter());
        for(UserItemInfo userItemInfo : getUserItemInfo(user)){
            userInfoRecyclerViewAdapter.add(userItemInfo);
        }
        userInfoRecyclerViewAdapter.notifyDataSetChanged();
    }

    private UserItemInfo[] getUserItemInfo(User user){
        return new UserItemInfo[]{
           new UserItemInfo(0x881E90FF, getString(R.string.text_me_info_sex), user.isSex()? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl)),
           new UserItemInfo(0x8800FF7F, getString(R.string.text_me_info_age), user.getAge() + getString(R.string.text_search_age)),
           new UserItemInfo(0x88FFD700, getString(R.string.text_me_info_birthday), user.getBirthday()),
           new UserItemInfo(0x88FF6347, getString(R.string.text_me_info_constellation), user.getConstellation()),
           new UserItemInfo(0x88F08080, getString(R.string.text_me_info_hobby), user.getHobby()),
           new UserItemInfo(0x8840E0D0, getString(R.string.text_me_info_status), user.getStatus()),
        };
    }

    private void configButtons(boolean isFriend){
        if(isFriend){
            findViewById(R.id.btn_add_friend).setVisibility(View.GONE);
            findViewById(R.id.ll_is_friend).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chat();
                }
            });
            findViewById(R.id.btn_audio_chat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioChat();
                }
            });
            findViewById(R.id.btn_video_chat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChat();
                }
            });
        }
        else {
            findViewById(R.id.btn_add_friend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend();
                }
            });
        }
    }

    private void addFriend(){
        final ModifiedDialog dialog = ModifiedDialog.createDefaultDialog(this, R.layout.dialog_send_friend);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        dialog.findViewById(R.id.tv_add_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAddingFriendMessage(((EditText)dialog.findViewById(R.id.et_msg)).getText().toString().trim());
                dialog.hide();
            }
        });
        dialog.show();
    }

    private void sendAddingFriendMessage(String message){
        IMessageService messageService = getBackendService(IMessageService.class);
        String targetId = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_ID);
        IMTextMessage imTextMessage = IMTextMessage.createPrivateTextMessage(targetId, message, AddingFriendRequestHandler.class);
        messageService.sendMessage(imTextMessage, null);
    }

    private void chat(){

    }

    private void audioChat(){

    }

    private void videoChat(){

    }


}
