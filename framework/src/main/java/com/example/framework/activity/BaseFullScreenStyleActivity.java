package com.example.framework.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.framework.R;


/**
 *
 * status bar is transparent
 * action bar is invisible
 * layout is full screen
 *
 *
 */

public class BaseFullScreenStyleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeFull);
        fixSystemUI();
    }


}
