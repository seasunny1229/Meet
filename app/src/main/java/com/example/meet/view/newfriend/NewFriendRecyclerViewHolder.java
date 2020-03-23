package com.example.meet.view.newfriend;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IMessageService;
import com.example.framework.backend.service.INewFriendManagementService;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.activity.NewFriendActivity;
import com.example.meet.handler.friend.AddingFriendAgreeHandler;
import com.example.meet.persistent.litepal.bean.NewFriend;
import com.example.meet.persistent.litepal.constant.LitePalConstant;

import java.util.List;

public class NewFriendRecyclerViewHolder extends RecyclerView.ViewHolder {

    private NewFriend newFriend;

    public NewFriendRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    // region layout
    void set(final NewFriend newFriend){
        this.newFriend = newFriend;

        // context
        final NewFriendActivity activity = ((NewFriendActivity) itemView.getContext());

        // query user
        IUserQueryService<User> userQueryService = activity.getBackendService(IUserQueryService.class);
        userQueryService.findUsersById(newFriend.getUid(), new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {

                // update user info
                updateUserInfo(activity, users.get(0));

                // set button
                setButton(newFriend.getStatus());
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(itemView.getContext(), e);
            }
        });

        // message
        ((TextView)itemView.findViewById(R.id.tv_msg)).setText(newFriend.getMessage());

    }

    private void updateUserInfo(Context context, User user){
        ((TextView)itemView.findViewById(R.id.tv_nickname)).setText(user.getNickName());
        ((TextView)itemView.findViewById(R.id.new_friend_tv_age)).setText(String.valueOf(user.getAge()));
        ((ImageView)itemView.findViewById(R.id.iv_sex)).setImageResource(user.isSex()? R.drawable.img_boy_icon : R.drawable.img_girl_icon);
        ((TextView)itemView.findViewById(R.id.tv_desc)).setText(user.getDesc());
        GlideUtil.load(context, user.getPhoto(), 100, 100, (ImageView)itemView.findViewById(R.id.iv_photo));
    }

    private void setButton(int status){
        switch (status){
            case LitePalConstant.NEW_FRIEND_STATUS_TO_BE_CONFIRMED:
                setButtonToBeConfirmed();
                break;
            case LitePalConstant.NEW_FRIEND_STATUS_AGREE:
                setTagAgree();
                break;
            case LitePalConstant.NEW_FRIEND_STATUS_DISAGREE:
                setTagDisagree();
                break;
             default:
                break;

        }
    }

    private void setButtonToBeConfirmed(){
        itemView.findViewById(R.id.ll_agree).setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.tv_result).setVisibility(View.GONE);
        itemView.findViewById(R.id.ll_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree();
            }
        });
        itemView.findViewById(R.id.ll_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disagree();
            }
        });
    }

    private void setTagAgree(){
        itemView.findViewById(R.id.ll_agree).setVisibility(View.GONE);
        itemView.findViewById(R.id.tv_result).setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.tv_result).setBackgroundResource(R.drawable.img_tag_yes_bg);
        ((TextView)itemView.findViewById(R.id.tv_result)).setText(R.string.text_new_friend_agree);
    }

    private void setTagDisagree(){
        itemView.findViewById(R.id.ll_agree).setVisibility(View.GONE);
        itemView.findViewById(R.id.tv_result).setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.tv_result).setBackgroundResource(R.drawable.img_tag_no_bg);
        ((TextView)itemView.findViewById(R.id.tv_result)).setText(R.string.text_new_friend_no_agree);
    }
    // endregion

    // region click action
    private void agree(){

        // context
        final NewFriendActivity activity = ((NewFriendActivity) itemView.getContext());

        // message service
        IMessageService messageService = activity.getBackendService(IMessageService.class);
        IMTextMessage imTextMessage = IMTextMessage.createPrivateTextMessage(newFriend.getUid(), "agree", AddingFriendAgreeHandler.class);
        messageService.sendMessage(imTextMessage, new BackendServiceCallback<Object>() {
            @Override
            public void success(Object o) {

            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });


        // update new friend info in LitePal


        // update UI by event bus

    }

    private void disagree(){

        // context
        final NewFriendActivity activity = ((NewFriendActivity) itemView.getContext());

        // update new friend info in LitePal
        newFriend.setStatus(LitePalConstant.NEW_FRIEND_STATUS_DISAGREE);
        newFriend.setTime(System.currentTimeMillis());

        // new friend service
        INewFriendManagementService service = activity.getBackendService(INewFriendManagementService.class);
        service.updateNewFriendStatus(newFriend, new BackendServiceCallback<NewFriend>() {
            @Override
            public void success(NewFriend newFriend) {
                setTagDisagree();
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });
    }

    // endregion

}
