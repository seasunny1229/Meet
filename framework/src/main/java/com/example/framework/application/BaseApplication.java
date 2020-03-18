package com.example.framework.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.framework.persistent.BaseSharedPreferenceConstant;

public class BaseApplication extends Application {


    public SharedPreferences getDefaultSharedPreference(){
        return  getSharedPreferences(BaseSharedPreferenceConstant.DEFAULT_NAME, Context.MODE_PRIVATE);
    }

}
