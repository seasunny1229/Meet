package com.example.meet.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.framework.activity.BaseCommonStyleActivity;
import com.example.framework.backend.manager.UserManager;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class SharePersonalQrCodeActivity extends BaseCommonStyleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_img);

        // 个人头像
        GlideUtil.load(this, UserManager.getInstance().getUser().getPhoto(), (ImageView) findViewById(R.id.iv_photo));

        // 二维码
        final ImageView qrCodeView = findViewById(R.id.iv_qrcode);
        qrCodeView.post(new Runnable() {
            @Override
            public void run() {
                String textContent = "http://baidu.com";
                Bitmap bitmap = CodeUtils.createImage(textContent, qrCodeView.getWidth(), qrCodeView.getHeight(), null);
                qrCodeView.setImageBitmap(bitmap);
            }
        });

    }
}
