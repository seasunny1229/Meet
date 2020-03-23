package com.example.framework.backend.messaging.converter;

import com.example.framework.backend.messaging.message.IMMessage;

public interface IMConversationInfoConverter<T> {

    void convertToRemoteConversationInfo(IMMessage imMessage, T t);

    void convertToLocalConversationInfo(IMMessage imMessage, T t);

}
