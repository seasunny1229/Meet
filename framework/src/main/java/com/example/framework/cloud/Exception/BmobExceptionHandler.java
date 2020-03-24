package com.example.framework.cloud.Exception;

import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.util.LogUtil;

public class BmobExceptionHandler {

    public static void handleBmobException(Throwable e){
        LogUtil.e("请求失败： " + e.getMessage());
        throw new BackendServiceException(e);
    }

}
