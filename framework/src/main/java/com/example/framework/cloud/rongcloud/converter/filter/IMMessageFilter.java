package com.example.framework.cloud.rongcloud.converter.filter;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMMessageFilter {

    boolean isValid(IMMessage imMessage);

}
