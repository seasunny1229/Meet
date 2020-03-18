package com.example.framework.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.framework.R;
import com.example.framework.view.listener.OnViewResultListener;

public class ImageVerificationView extends View {
    private static final int DEFAULT_DRAGGABLE_X = 200,  DEFAULT_ERROR_VALUE = 10;

    // bitmap
    private Bitmap adjustedBackgroundBitmap;

    // paint
    private Paint paint;

    // listener
    private OnViewResultListener onViewResultListener;

    // parameter
    private float mWidth, mHeight;
    private float blankLeft, blankTop, blankSize;
    private int draggableX;

    public ImageVerificationView(Context context){
        super(context);
        init();
    }

    public ImageVerificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageVerificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnViewResultListener(OnViewResultListener onViewResultListener) {
        this.onViewResultListener = onViewResultListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundImage(canvas);
        drawBlankImage(canvas);
        drawDraggableImage(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX() > 0 && event.getX() < mWidth - blankSize){
                    draggableX = (int) event.getX();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(draggableX > blankLeft - DEFAULT_ERROR_VALUE && draggableX < blankLeft + DEFAULT_ERROR_VALUE){
                    if(onViewResultListener != null){
                        onViewResultListener.onResult(null);
                    }
                }
                break;

        }
        return true;
    }

    private void init(){
        paint = new Paint();
        draggableX = DEFAULT_DRAGGABLE_X;
    }


    private void drawBackgroundImage(Canvas canvas){

        // get background image bitmap
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_bg);

        // adjusted background bitmap
        adjustedBackgroundBitmap = Bitmap.createBitmap((int)mWidth, (int)mHeight, Bitmap.Config.ARGB_8888);
        Canvas adjustedBackgroundCanvas = new Canvas(adjustedBackgroundBitmap);
        adjustedBackgroundCanvas.drawBitmap(backgroundBitmap, null, new Rect(0,0, (int) mWidth, (int) mHeight), paint);

        // draw adjusted background bitmap on canvas
        canvas.drawBitmap(adjustedBackgroundBitmap, null, new Rect(0,0, getWidth(), getHeight()), paint);
    }



    private void drawBlankImage(Canvas canvas){

        // get background image bitmap
        Bitmap blankBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_null_card);

        // size
        blankSize = blankBitmap.getWidth();

        // left
        blankLeft = mWidth / 3 * 2;

        // top
        blankTop =  mHeight / 2  - blankSize / 2;

        // draw blank bitmap on canvas
        canvas.drawBitmap(blankBitmap, blankLeft, blankTop, paint);
    }

    private void drawDraggableImage(Canvas canvas){

        // get draggable bitmap from background bitmap
        Bitmap draggableBitmap = Bitmap.createBitmap(adjustedBackgroundBitmap, (int) blankLeft, (int) blankTop, (int) blankSize, (int) blankSize);

        // draw draggable bitmap on canvas
        canvas.drawBitmap(draggableBitmap, draggableX, blankTop, paint);
    }


}
