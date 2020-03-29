package com.example.meet.handler.chat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.backend.messaging.message.IMTextMessage;
import com.example.framework.backend.service.IUserQueryService;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.framework.exception.ExceptionHandler;
import com.example.framework.glide.GlideOnResultListener;
import com.example.framework.glide.GlideUtil;
import com.example.framework.notification.NotificationUtil;
import com.example.meet.activity.ChatActivity;
import com.example.meet.application.MainApplication;
import com.example.meet.eventbus.EventBusConstant;
import com.example.meet.notification.NotificationConstant;

import java.util.List;

public class ChatHandler extends BaseIMMessageReceivingHandler {


    @Override
    public void handleIMMessage(final Context context, final IMMessage imMessage) {

        // post event to update chat activity
        EventBusUtil.post(new MessageEvent(EventBusConstant.UPDATE_CHAT_INFO, imMessage));

        // user info
        IUserQueryService<User> userQueryService = ((MainApplication)context).getBackendService(IUserQueryService.class);
        userQueryService.findUsersById(imMessage.getSourceId(), new BackendServiceCallback<List<User>>() {
            @Override
            public void success(List<User> users) {
                if(users != null && !users.isEmpty()){
                    notification(context, imMessage, users.get(0));
                }
            }

            @Override
            public void fail(BackendServiceException e) {
                ExceptionHandler.handleBackendServiceException(context, e);
            }
        });
    }

    private void notification(final Context context, final IMMessage imMessage, final User user){

        // load portrait
        GlideUtil.loadUrlToBitmap(context, user.getPhoto(), new GlideOnResultListener() {
            @Override
            public void onResourceReady(Bitmap bitmap) {

                // pending intent
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_ID, user.getUid());
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_PHOTO, user.getPhoto());
                intent.putExtra(ChatActivity.INTENT_EXTRA_FRIEND_USER_NAME, user.getNickName());
                PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);

                // notification
                NotificationUtil.pushMessageNotification(context, NotificationConstant.CHANNEL_MESSAGE, user.getUid(), user.getNickName(), getText(imMessage), bitmap, pendingIntent);
            }
        });
    }


    private String getText(IMMessage imMessage){
        String text = NotificationConstant.CHANNEL_MESSAGE_NAME;
        if(imMessage instanceof IMTextMessage){
            text = ((IMTextMessage) imMessage).getMessageContent();
        }
        return text;
    }


}
