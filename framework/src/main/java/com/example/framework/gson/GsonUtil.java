package com.example.framework.gson;

import com.google.gson.Gson;

public class GsonUtil {

    private static Gson gson = new Gson();

    public static String toJson(Object object){
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }

}
