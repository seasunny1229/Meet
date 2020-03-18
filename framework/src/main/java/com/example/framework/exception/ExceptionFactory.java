package com.example.framework.exception;

import com.example.framework.R;
import com.example.framework.backend.exception.BackendServiceException;

public class ExceptionFactory {

    public static BackendServiceException createNotifyUserBackendServiceException(){
        BackendServiceException exception = new BackendServiceException(R.string.backend_system_error);
        exception.setErrorCode(R.integer.notify_user_error_info);
        return exception;
    }

    public static BackendServiceException createNotifyUserBackendServiceException(String message){
        BackendServiceException exception = new BackendServiceException(message);
        exception.setErrorCode(R.integer.notify_user_error_info);
        return exception;
    }

    public static BackendServiceException createNotifyUserBackendServiceException(Throwable e){
        BackendServiceException exception = new BackendServiceException(e);
        exception.setErrorCode(R.integer.notify_user_error_info);
        return exception;
    }

    public static BackendServiceException createNotNotifyUserBackendServiceException(){
        BackendServiceException exception = new BackendServiceException();
        exception.setErrorCode(R.integer.not_notify_user_error_info);
        return exception;
    }

    public static BackendServiceException createNotNotifyUserBackendServiceException(String message){
        BackendServiceException exception = new BackendServiceException(message);
        exception.setErrorCode(R.integer.not_notify_user_error_info);
        return exception;
    }

    public static BackendServiceException createNotNotifyUserBackendServiceException(Throwable e){
        BackendServiceException exception = new BackendServiceException(e);
        exception.setErrorCode(R.integer.not_notify_user_error_info);
        return exception;
    }

}
