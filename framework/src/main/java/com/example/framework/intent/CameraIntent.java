package com.example.framework.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.example.framework.provider.FileProviderUtil;

import java.io.File;

public class CameraIntent {

    public static Intent camera(Activity activity, File file){

        // start camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // uri
        Uri uri = FileProviderUtil.getUriFromFile(activity, file);

        //添加权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        // intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return intent;
    }


}
