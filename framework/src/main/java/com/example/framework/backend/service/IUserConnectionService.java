package com.example.framework.backend.service;

import com.example.framework.backend.callback.BackendServiceCallback;

public interface IUserConnectionService {

    void connect(String token, BackendServiceCallback<String> backendServiceCallback);


}
