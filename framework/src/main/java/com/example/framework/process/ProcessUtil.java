package com.example.framework.process;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

public class ProcessUtil {

    // 获取当前进程名称
    public static String getCurrentProcessName(Context context){
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager == null){
            return null;
        }
        for(ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()){
               if(appProcessInfo.pid == pid){
                   return appProcessInfo.processName;
               }
        }
        return null;
    }



}
