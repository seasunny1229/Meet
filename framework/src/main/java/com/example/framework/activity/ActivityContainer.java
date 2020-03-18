package com.example.framework.activity;

import android.app.Activity;

import java.util.HashSet;

public class ActivityContainer {

    private static ActivityContainer mInstance;


    public static ActivityContainer getInstance(){
        if(mInstance == null){
            mInstance = new ActivityContainer();
        }
        return mInstance;
    }

    private HashSet<Activity> activitySet;

    private ActivityContainer(){
        activitySet = new HashSet<>();
    }

    public void addActivity(Activity activity){
        activitySet.add(activity);
    }

    public void exit(){
        for(Activity activity : activitySet){
            if(activity != null){
                activity.finish();
            }
        }
    }

}
