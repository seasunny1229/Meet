package com.example.framework.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.framework.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    // date类
    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     *
     * 打印到console
     *
     */
    public static void i(String text){
        if(BuildConfig.LOG_DEBUG){
            if(!TextUtils.isEmpty(text)){
                Log.i(BuildConfig.LOG_TAG, text);
            }
        }
    }

    public static void e(String text){
        if(BuildConfig.LOG_DEBUG){
            if(!TextUtils.isEmpty(text)){
                Log.e(BuildConfig.LOG_TAG, text);
            }
        }
    }


    /**
    *
    * 写入内存卡
    * @param text
    */
    private static void writeToFile(String text){


        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;

        try {

            // 文件路径
            String fileRoot = Environment.getExternalStorageDirectory().getPath() + "/Meet/";

            // 文件名
            String fileName = "Meet.log";

            // 时间 + 内容
            String log = mSimpleDateFormat.format(new Date()) + " " + text + "\n";

            // 检查父路径
            File fileGroup = new File(fileRoot);

            // 检查跟目录
            if(!fileGroup.exists()){
                fileGroup.mkdirs();
            }

            // 创建文件
            File fileChild = new File(fileRoot + fileName);
            if(!fileChild.exists()){
                fileChild.createNewFile();
            }

            fileOutputStream = new FileOutputStream(fileRoot + fileName, true);

            // 编码问题，GBK
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Charset.forName("gbk")));
            bufferedWriter.write(log);


        }catch (FileNotFoundException e){
            e.printStackTrace();
            e(e.toString());
        }

        catch (IOException e){
            e.printStackTrace();
            e(e.toString());
        }finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }


    }




}
