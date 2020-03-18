package com.example.framework.backend.application;

public interface IBackendService {

    <T> T getBackendService(Class<T> clazz);

}
