package com.example.framework.cloud.rongcloud.converter.filter;

import android.text.TextUtils;

import com.example.framework.backend.messaging.info.ExtraInfo;
import com.example.framework.cloud.rongcloud.converter.manager.RongCloudMessageConverterManager;
import com.example.framework.gson.GsonUtil;

import io.rong.imlib.model.MessageContent;


public class MessageFilter {

    public static boolean isMessageValid(RongCloudMessageConverterManager manager, MessageContent messageContent){
        if(!manager.hasExtraInfoConverter(messageContent.getClass())){
            return false;
        }

        String extra = manager.getExtraInfoConverter(messageContent.getClass()).getExtraInfo(messageContent);
        if(TextUtils.isEmpty(extra)){
            return false;
        }

        try{
            GsonUtil.fromJson(extra, ExtraInfo.class);
        }catch (Exception e){
            return false;
        }

        return true;
    }

}
