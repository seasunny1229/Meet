package com.example.framework.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
                .placeholder(R.drawable.img_glide_load_error2)
                .error(R.drawable.img_glide_load_error2)
                .format(DecodeFormat.PREFER_RGB_565)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                //加载缩略图
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void load(Context mContext, String url, int w, int h, ImageView imageView, int placeholderResId, int loadResId) {
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .override(w, h)
                .placeholder(placeholderResId)
                .error(loadResId)
                .format(DecodeFormat.PREFER_RGB_565)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                //加载缩略图
                .thumbnail(0.3f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadUrlToBitmap(Context mContext, String url, final GlideOnResultListener listener) {
            if (mContext != null) {
                Glide.with(mContext.getApplicationContext()).asBitmap().load(url).centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .format(DecodeFormat.PREFER_RGB_565)
                        // 取消动画，防止第一次加载不出来
                        .dontAnimate()
                        //加载缩略图
                        .thumbnail(0.3f)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                if (null != listener) {
                                    listener.onResourceReady(resource);
                                }
                            }
                        });
            }
    }


}
