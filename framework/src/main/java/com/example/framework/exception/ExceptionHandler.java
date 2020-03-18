package com.example.framework.exception;

import android.content.Context;

import com.example.framework.R;
import com.example.framework.backend.exception.BackendServiceException;
import com.example.framework.util.ToastUtil;

public class ExceptionHandler {

    public static void handleBackendServiceException(Context context, BackendServiceException e){
        if(e.getErrorCode() == R.integer.notify_user_error_info){
            ToastUtil.toast(context, e.getMessage());
        }
    }



}
