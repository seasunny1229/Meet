package com.example.meet.handler.chat;

import android.content.Context;

import com.example.framework.backend.messaging.handler.base.BaseIMMessageReceivingHandler;
import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.eventbus.EventBusUtil;
import com.example.framework.eventbus.MessageEvent;
import com.example.meet.eventbus.EventBusConstant;

public class ChatHandler extends BaseIMMessageReceivingHandler {


    @Override
    public void handleIMMessage(Context context, IMMessage imMessage) {

        // post event to update chat activity
        EventBusUtil.post(new MessageEvent(EventBusConstant.UPDATE_CHAT_INFO, imMessage));
    }
}
