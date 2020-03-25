package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;
import com.example.framework.backend.messaging.conversation.IMConversation;

import java.util.List;

public interface IConversationService {


    void getConversationList(BackendServiceCallback<List<IMConversation>> backendServiceCallback);

}
