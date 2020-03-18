package com.example.framework.view.controller;

import android.content.Context;

import com.example.framework.R;
import com.example.framework.view.custom.ImageVerificationView;
import com.example.framework.view.custom.ModifiedDialog;
import com.example.framework.view.listener.OnViewResultListener;

public class ImageVerificationDialogController extends DialogController {

    private OnViewResultListener onViewResultListener;

    public ImageVerificationDialogController(Context context) {
        super(context);
    }

    @Override
    protected void create(Context context) {

        // create image verification dialog
        dialog = ModifiedDialog.createDefaultDialog(context, R.layout.layout_image_verification_dialog);
        dialog.setCancelable(false);

        // image verification view
        final ImageVerificationView imageVerificationView = dialog.findViewById(R.id.image_verification);
        imageVerificationView.setOnViewResultListener(new OnViewResultListener() {
            @Override
            public void onResult(Object object) {
                dialog.hide();
                if(onViewResultListener != null){
                    onViewResultListener.onResult(null);
                }
            }
        });
    }

    public void setOnViewResultListener(OnViewResultListener onViewResultListener) {
        this.onViewResultListener = onViewResultListener;
    }
}
