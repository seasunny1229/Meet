package com.example.framework.cloud.Exception;

import com.example.framework.backend.exception.BackendServiceException;

import io.rong.imlib.RongIMClient;
import io.rong.models.Result;

public class CloudExceptionHandler {


    // region rong cloud
    public static void handleRongIMClientException(RongIMClient.ErrorCode errorCode){
        throw new BackendServiceException(errorCode.getMessage());
    }

    public static void handleRongCloudServerException(Throwable e){
        throw new BackendServiceException(e);
    }

    public static void handlerRongCloudServerException(Result result){
        throw new BackendServiceException(result.getErrorMessage());
    }

    // endregion




}
