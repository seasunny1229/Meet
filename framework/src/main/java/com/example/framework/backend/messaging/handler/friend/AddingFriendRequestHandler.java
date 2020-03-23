package com.example.framework.backend.messaging.handler.friend;

import com.example.framework.backend.messaging.message.IMMessage;
import com.example.framework.util.LogUtil;

public class AddingFriendRequestHandler extends BaseFriendManagementHandler {


    @Override
    public void handleIMMessage(IMMessage imMessage) {
        LogUtil.i("添加好友请求：" + imMessage.toString());
    }
}
