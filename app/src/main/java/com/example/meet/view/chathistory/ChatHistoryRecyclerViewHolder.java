package com.example.meet.view.chathistory;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.activity.BaseActivity;
import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.conversation.IMConversation;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.activity.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatHistoryRecyclerViewHolder extends RecyclerView.ViewHolder  {


    public ChatHistoryRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(final IMConversation imConversation){
        final String targetId = imConversation.getTargetId();

        final BaseActivity activity = (BaseActivity) itemView.getContext();
        IUserQueryService<User> userIUserQueryService = activity.getBackendService(IUserQueryService.class);
        userIUserQueryService.findUsersById(targetId, new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                setPersonalInfo(users.get(0));
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(activity, e);
            }
        });

        if(imConversation.getUnreadMessageCount() == 0){
            itemView.findViewById(R.id.tv_un_read).setVisibility(View.GONE);

        }
        else {
            itemView.findViewById(R.id.tv_un_read).setVisibility(View.VISIBLE);
            ((TextView)itemView.findViewById(R.id.tv_un_read)).setText(String.valueOf(imConversation.getUnreadMessageCount()));
        }
        ((TextView)itemView.findViewById(R.id.tv_content)).setText(getContent(imConversation.getLatestIMMessage()));
        ((TextView)itemView.findViewById(R.id.tv_time)).setText(new SimpleDateFormat("HH:mm:ss").format(imConversation.getReceivedTime()));
    }

    private void setPersonalInfo(final User user){
        ((TextView)itemView.findViewById(R.id.tv_nickname)).setText(user.getNickName());
        GlideUtil.load(itemView.getContext(), user.getPhoto(), 100, 100, (ImageView) itemView.findViewById(R.id.iv_photo));

        // click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_ID, user.getUid());
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_NAME, user.getNickName());
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_PHOTO, user.getPhoto());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private String getContent(IMMessage imMessage){
        String content = "";
        if(imMessage instanceof IMTextMessage){
            content = ((IMTextMessage) imMessage).getMessageContent();
        }
        return content;
    }

}
