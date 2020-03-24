package com.example.framework.cloud.rongcloud.receiver;

import android.content.Context;

import com.example.framework.util.LogUtil;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 *
 *
 * 广播接收器
 * 当应用处于后台运行或者和融云服务器 disconnect() 的时候，收到消息，SDK 会以通知形式提醒您。
 * 所以需要自定义一个继承 PushMessageReceiver 的广播接收器，用来接收提醒通知。
 *
 */
public class SealNotificationReceiver extends PushMessageReceiver {

    // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        LogUtil.e("显示通知: " + pushNotificationMessage.getPushContent());
        return true;
    }

    // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return true;
    }
}
