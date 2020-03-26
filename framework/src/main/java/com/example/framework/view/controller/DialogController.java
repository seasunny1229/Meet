package com.example.framework.view.controller;

import android.content.Context;

import com.example.framework.view.custom.ModifiedDialog;

public abstract class DialogController {

    protected ModifiedDialog dialog;

    public DialogController(Context context){
        create(context);
    }

    protected abstract void create(Context context);

    public void show(){
        dialog.show();
    };

    public void hide(){
        dialog.dismiss();
    };


}
