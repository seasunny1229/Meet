package com.example.framework.provider;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import java.util.HashMap;

public class ContentProviderUtil {


    /**
     *
     * 通过Uri去系统查询图片真实地址
     *
     */
    public static String getRealPathFromURI(Context context, Uri uri) {
        String realPath = "";
        try {
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    realPath = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPath;
    }


    /**
     *
     * 获取通讯录好友
     *
     */
    public static HashMap<String, String> getContactData(Activity activity) {
        HashMap<String, String> contactMap = new HashMap<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if(cursor == null){
            return null;
        }
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ", "").replace("-", "");
            contactMap.put(name, phone);
        }
        cursor.close();
        return contactMap;
    }


}
