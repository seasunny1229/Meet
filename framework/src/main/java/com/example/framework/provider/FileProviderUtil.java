package com.example.framework.provider;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileProviderUtil {

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.framework.fileProvider";

    public static Uri getUriFromFile(Activity activity, File file){

        Uri uri;

        // 兼容N
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            uri = Uri.fromFile(file);
        }
        else {
            uri = FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTHORITY, file);
        }

        return uri;
    }

}
