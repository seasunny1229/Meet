package com.example.framework.backend.messaging.handler.base;

import android.content.Context;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMMessageReceivingHandler {

    void handleIMMessage(Context context, IMMessage imMessage);

}
