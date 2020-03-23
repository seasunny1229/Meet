package com.example.framework.cloud.rongcloud.converter.extra;

import io.rong.imlib.model.MessageContent;

public interface IExtraInfoConverter {

    void setExtraInfo(MessageContent messageContent, String extra);

    String getExtraInfo(MessageContent messageContent);

}
