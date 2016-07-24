package com.tarellano.kayak.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tarellano.kayak.R;

/**
 * Created by Thomas Arellano on 7/2/2016.
 */
public class KMapView extends View {

    private Paint mBackgroundPaint;
    private Paint mLinePaint;


    private Path mPath = new Path();

    public KMapView(Context context) {
        super(context);
        init();
    }

    public KMapView(Context context, AttributeSet st) {
        super(context, st);
        init();
    }

    private void init(){
        mBackgroundPaint = new Paint(0);
        mBackgroundPaint.setColor(0xff515151);
        mBackgroundPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint(0);
        mLinePaint.setColor(getResources().getColor(R.color.bluegrass));
        mLinePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //canvas.drawLine(0,0, getWidth(), getHeight(), mBackgroundPaint);

        canvas.drawRect(0, 0, getWidth(), getHeight(), mLinePaint);
        canvas.drawPath(mPath, mBackgroundPaint);

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float touchX = motionEvent.getX();
        float touchY = motionEvent.getY();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }
}
