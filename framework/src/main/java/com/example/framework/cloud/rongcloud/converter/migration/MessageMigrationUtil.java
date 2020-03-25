package com.example.framework.cloud.rongcloud.converter.migration;

import android.text.TextUtils;

import com.example.framework.backend.messaging.info.ExtraInfo;
import com.example.framework.cloud.rongcloud.converter.extra.ExtraInfoUtil;
import com.example.framework.cloud.rongcloud.converter.manager.RongCloudMessageConverterManager;
import com.example.framework.gson.GsonUtil;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;


public class MessageMigrationUtil {

    public static boolean isMessageVersionValid(RongCloudMessageConverterManager manager, MessageContent messageContent){

        // cannot find extra info converter in map
        if(!manager.hasExtraInfoConverter(messageContent.getClass())){
            return false;
        }

        // has not extra info
        String extra = manager.getExtraInfoConverter(messageContent.getClass()).getExtraInfo(messageContent);
        if(TextUtils.isEmpty(extra)){
            return false;
        }

        // cannot convert extra info from json to object
        try{
            GsonUtil.fromJson(extra, ExtraInfo.class);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    public static void migrateExtraInfo(RongCloudMessageConverterManager manager, Message message){

        // new extra info
        ExtraInfo extraInfo = new ExtraInfo();

        // set IM message type
        extraInfo.setMessageTypeName(ExtraInfoUtil.convertToIMMessageType(message).name());

        // set IM conversation type
        extraInfo.setConversationTypeName(ExtraInfoUtil.convertToIMConversationType(message).name());

        // get extra info converter via message content type
        // convert extra info object to json
        // put new json extra info into message content
        manager.getExtraInfoConverter(message.getContent().getClass()).setExtraInfo(message.getContent(), GsonUtil.toJson(extraInfo));
    }



}
