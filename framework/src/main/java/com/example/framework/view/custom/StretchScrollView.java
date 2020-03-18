package com.example.framework.view.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.framework.util.LogUtil;

public abstract class StretchScrollView extends ScrollView {
    private static final float STRETCH_SCROLL_RATE = 0.3f;
    private static final float REVERT_RATE = 0.5f;

    private View stretchView;

    private int orgStretchViewWidth, orgStretchViewHeight;

    private float touchInitPosY;

    public StretchScrollView(Context context) {
        super(context);
    }

    public StretchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected final void onFinishInflate() {
        super.onFinishInflate();

        // get target stretch view
        stretchView = getStretchView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // the first time to set original view width and height
        if(orgStretchViewWidth == 0 || orgStretchViewHeight == 0){
            orgStretchViewWidth = stretchView.getMeasuredWidth();
            orgStretchViewHeight = stretchView.getMeasuredHeight();
        }
    }


    protected abstract View getStretchView();


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(stretchView == null){
            return false;
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchInitPosY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float stretchDistance = (ev.getY() - touchInitPosY) * STRETCH_SCROLL_RATE;
                if(stretchDistance < 0){
                    break;
                }
                setStretchViewLayoutParams(stretchDistance);
                break;
            case MotionEvent.ACTION_UP:
                revert();
                break;
        }
        return true;
    }

    private void setStretchViewLayoutParams(float stretchDistance){
        ViewGroup.LayoutParams params = stretchView.getLayoutParams();
        params.width = (int) (orgStretchViewWidth + stretchDistance);
        params.height = (int) (orgStretchViewHeight *  ((orgStretchViewWidth + stretchDistance) /  orgStretchViewWidth));
        ((MarginLayoutParams)params).setMargins(-(params.width - orgStretchViewWidth)/2,0,0,0);
        LogUtil.i(String.valueOf(-(params.width)/2));
        stretchView.setLayoutParams(params);
    }

    private void revert(){
        int distance = stretchView.getMeasuredWidth() - orgStretchViewWidth;
        ValueAnimator animator = ValueAnimator.ofFloat(distance, 0).setDuration((long) (distance * REVERT_RATE));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setStretchViewLayoutParams((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
