package com.example.framework.cloud.Exception;

import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.util.LogUtil;

import io.rong.imlib.RongIMClient;
import io.rong.models.Result;

public class CloudExceptionHandler {


    // region rong cloud
    public static void handleRongIMClientException(RongIMClient.ErrorCode errorCode){
        String errorMessage = "请求失败,  错误代码：" + errorCode.getValue() + " 错误信息： " + errorCode.getMessage();
        LogUtil.e(errorMessage);
        throw new BackendServiceException(errorCode.getMessage());
    }

    public static void handleRongCloudServerException(Throwable e){
        LogUtil.e("请求失败： " + e.getMessage());
        throw new BackendServiceException(e);
    }

    public static void handlerRongCloudServerException(Result result){
        LogUtil.e("请求失败： " + result.errorMessage);
        throw new BackendServiceException(result.getErrorMessage());
    }

    // endregion




}
