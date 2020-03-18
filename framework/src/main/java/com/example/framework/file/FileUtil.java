package com.example.framework.file;


import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FileUtil {

    // date format
    private  static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);

    // region file
    public static File newTempFile(){
        return new File(Environment.getExternalStorageDirectory(), getTempFileName());
    }

    public static File newTempFile(String name){
        return new File(Environment.getExternalStorageDirectory(), name);
    }

    private static String getTempFileName(){
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    // endregion



}
