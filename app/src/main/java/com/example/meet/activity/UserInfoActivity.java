package com.example.meet.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseActivity;
import com.example.framework.activity.BaseFullScreenStyleActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserInfoService;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;

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

    // layout
    private static final int[] BACKGROUND_COLORS = {0x881E90FF, 0x8800FF7F, 0x88FFD700, 0x88FF6347, 0x88F08080, 0x8840E0D0};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 更新用户信息
        updateUserInfo();

    }

    private void updateUserInfo(){

        final String userId = getIntent().getStringExtra(INTENT_EXTRA_FRIEND_USER_ID);

        IUserQueryService<User> useQueryService = getBackendService(IUserQueryService.class);
        useQueryService.findUsersById(userId, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {

                // user
                User user = users.get(0);

                TextView nicknameView = findViewById(R.id.tv_nickname);
                nicknameView.setText(user.getNickName());

                CircleImageView portraitView = findViewById(R.id.iv_user_photo);
                GlideUtil.load(UserInfoActivity.this, user.getPhoto(), 100,  100, portraitView);

            }

            @Override
            public void fail(BackendServiceException e) {

            }
        });
    }





}
