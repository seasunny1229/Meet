package com.example.framework.util;

import android.content.Context;
import android.widget.Toast;

import com.example.framework.BuildConfig;

public class ToastUtil {

    // show short toast
    public static void toast(Context context, int resId){
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // show short toast in debug mode
    public static void toastInDebugMode(Context context, int resId){
        if(BuildConfig.DEBUG){
            toast(context, resId);
        }
    }

    public static void toastInDebugMode(Context context, String message){
        if(BuildConfig.DEBUG){
            LogUtil.i(message);
            toast(context, message);
        }
    }

}
