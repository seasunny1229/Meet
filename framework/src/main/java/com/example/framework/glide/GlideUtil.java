package com.example.framework.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.framework.R;

public class GlideUtil {

    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context.getApplicationContext())
                .load(url)
                .placeholder(R.drawable.img_glide_load_ing)
                .error(R.drawable.img_glide_load_error)
                .format(DecodeFormat.PREFER_RGB_565)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                //加载缩略图
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 加载图片Url
     *
     */
    public static void load(Context mContext, String url, int w, int h, ImageView imageView) {
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .override(w, h)
                .placeholder(R.drawable.img_glide_load_ing)
                .error(R.drawable.img_glide_load_error)
                .format(DecodeFormat.PREFER_RGB_565)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                //加载缩略图
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


}
