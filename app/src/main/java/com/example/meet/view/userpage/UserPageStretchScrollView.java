package com.example.meet.view.userpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.framework.view.custom.StretchScrollView;
import com.example.meet.R;

public class UserPageStretchScrollView extends StretchScrollView {

    public UserPageStretchScrollView(Context context) {
        super(context);
    }

    public UserPageStretchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserPageStretchScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getStretchView() {
        return findViewById(R.id.ll_user_page_head);
    }
}
