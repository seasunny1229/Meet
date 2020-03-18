package com.example.framework.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.framework.R;

public class ModifiedDialog extends Dialog {

    public static ModifiedDialog createDefaultDialog(Context context, int layout){
        return new ModifiedDialog(context, R.style.Theme_Dialog, layout, Gravity.CENTER);
    }

    public static ModifiedDialog createDefaultDialog(Context context, int layout, int gravity){
        return new ModifiedDialog(context, R.style.Theme_Dialog, layout, gravity);
    }


    public ModifiedDialog(@NonNull Context context, int themeResId, int layout, int gravity) {
        super(context, themeResId);
        setContentView(layout);

        // set window parameters
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);

        // is not cancelable
        setCancelable(false);
    }



}
