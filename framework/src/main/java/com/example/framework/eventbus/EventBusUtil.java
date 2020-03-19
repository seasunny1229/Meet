package com.example.framework.eventbus;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {

    public static void registerEventBus(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }

    public static void unregisterEventBus(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(MessageEvent messageEvent){
        EventBus.getDefault().post(messageEvent);
    }

    public static void postSticky(MessageEvent messageEvent){
        EventBus.getDefault().postSticky(messageEvent);
    }

}
