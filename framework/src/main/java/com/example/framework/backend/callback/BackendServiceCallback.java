package com.example.framework.backend.callback;

import com.example.framework.backend.exception.BackendServiceException;

public interface BackendServiceCallback<T> {

    void success(T t);

    void fail(BackendServiceException e);
}
