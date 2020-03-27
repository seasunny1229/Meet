package com.example.framework.cloud.bmob.util;

import com.example.framework.backend.pushing.bean.PushInfo;
import com.example.framework.backend.pushing.constant.PushType;
import com.example.framework.cloud.bmob.bean.IMPush;

public class PushInfoUtil {

    public static PushInfo covertToLocalData(IMPush push){

        PushInfo pushInfo = new PushInfo();
        pushInfo.setType(PushType.valueOf(push.getType()));
        pushInfo.setUid(push.getUid());
        pushInfo.setTime(push.getTime());
        pushInfo.setText(push.getText());
        pushInfo.setMediaUrl(push.getMediaUrl());

        return pushInfo;
    }

}
