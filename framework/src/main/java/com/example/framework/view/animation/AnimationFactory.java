package com.example.framework.view.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationFactory {


    public static ObjectAnimator rotation(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).setDuration(2 * 1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        return objectAnimator;
    }


}



