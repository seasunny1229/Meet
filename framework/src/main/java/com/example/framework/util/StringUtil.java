package com.example.framework.util;

public class StringUtil {


    public static boolean isTelephoneValid(String phone){
        return phone.matches("[\\d]{11}");
    }



}
