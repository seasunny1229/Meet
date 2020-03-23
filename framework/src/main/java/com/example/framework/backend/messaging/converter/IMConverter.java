package com.example.framework.backend.messaging.converter;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMConverter<T> {

    void convertToRemoteMessage(IMMessage imMessage, T t);

    void convertToLocalMessage(IMMessage imMessage, T t);
}
