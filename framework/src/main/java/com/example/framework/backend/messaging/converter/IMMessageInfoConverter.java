package com.example.framework.backend.messaging.converter;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMMessageInfoConverter<T> {

    void convertToRemoteMessageInfo(IMMessage imMessage, T t);

    void convertToLocalMessageInfo(IMMessage imMessage, T t);


}
