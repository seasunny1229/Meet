package com.example.framework.cloud.rongcloud.converter.extra;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class TextMessageExtraInfoConverter implements IExtraInfoConverter {


    @Override
    public void setExtraInfo(MessageContent messageContent, String extra) {
        ((TextMessage)messageContent).setExtra(extra);
    }

    @Override
    public String getExtraInfo(MessageContent messageContent) {
        return ((TextMessage)messageContent).getExtra();
    }
}
