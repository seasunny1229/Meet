package com.example.framework.intent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.example.framework.provider.FileProviderUtil;

import java.io.File;

public class PhotoIntent {

    public static Intent selectPhotoFromAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    public static Intent cropPhoto(Activity activity, File rawFile, File croppedFile){

        Intent intent = new Intent("com.android.camera.action.CROP");

        // get uri from file provider
        Uri uri = FileProviderUtil.getUriFromFile(activity, rawFile);

        // data, type
        intent.setDataAndType(uri, "image/*");

        //设置裁剪
        intent.putExtra("crop", "true");

        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);

        //兼容android 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        //单独存储裁剪文件，解决手机兼容性问题
        // 获取URI
        Uri croppedUri = Uri.parse("file://" + "/" + croppedFile.getPath());

        // 输出文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, croppedUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        return intent;
    }


}
