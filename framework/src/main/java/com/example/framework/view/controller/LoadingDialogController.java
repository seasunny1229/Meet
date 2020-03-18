package com.example.framework.view.controller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.R;
import com.example.framework.view.animation.AnimationFactory;
import com.example.framework.view.custom.ModifiedDialog;

public class LoadingDialogController extends DialogController{

    // animation
    private ObjectAnimator rotationAnimator;

    public LoadingDialogController(Context context){
        super(context);
    }

    @Override
    protected void create(Context context){

        // create loading dialog view
        dialog = ModifiedDialog.createDefaultDialog(context, R.layout.layout_loading_dialog);
        dialog.setCancelable(false);

        // create rotation animator
        ImageView loadingImageView = dialog.findViewById(R.id.iv_loading);
        rotationAnimator = AnimationFactory.rotation(loadingImageView);
    }

    @Override
    public void show(){
        super.show();
        rotationAnimator.start();
    }

    @Override
    public void hide(){
        super.hide();
        rotationAnimator.pause();
    }

    public LoadingDialogController setLoadingText(String text){
        if(!TextUtils.isEmpty(text)){
            TextView loadingTextView = dialog.findViewById(R.id.tv_loading_text);
            loadingTextView.setText(text);
        }
        return this;
    }

}



