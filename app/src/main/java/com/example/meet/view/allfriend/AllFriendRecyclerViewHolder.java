package com.example.meet.view.allfriend;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.Friend;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.activity.UserInfoActivity;

import java.util.List;

public class AllFriendRecyclerViewHolder extends RecyclerView.ViewHolder {


    public AllFriendRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(final Friend friend){
        final BaseActivity activity = (BaseActivity) itemView.getContext();
        IUserQueryService<User> userIUserQueryService = activity.getBackendService(IUserQueryService.class);
        userIUserQueryService.findUsersById(friend.getId(), new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                updateUserInfo(users.get(0));
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.INTENT_EXTRA_FRIEND_USER_ID, friend.getId());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private void updateUserInfo(User user){
        ((TextView) itemView.findViewById(R.id.tv_nickname)).setText(user.getNickName());
        ((TextView) itemView.findViewById(R.id.tv_desc)).setText(user.getDesc());
        ((ImageView)itemView.findViewById(R.id.iv_sex)).setImageResource(user.isSex()?R.drawable.img_boy_icon: R.drawable.img_girl_icon);
        GlideUtil.load(itemView.getContext(), user.getPhoto(), 100, 100, (ImageView) itemView.findViewById(R.id.iv_photo));
    }
}
