package com.tarellano.kayak.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tarellano.kayak.R;

/**
 * Created by Thomas Arellano on 7/2/2016.
 */
public class KMapView extends View {

    private final static String TAG = "kmapView";

    private Paint mLinePaint;
    private Paint mBackgroundPaint;

    private static final int STROKE_WIDTH = 8;

    private Rect map;


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

        mLinePaint = new Paint(0);
        mLinePaint.setColor(0xff515151);
        mLinePaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(STROKE_WIDTH);

        mBackgroundPaint = new Paint(0);
        mBackgroundPaint.setColor(getResources().getColor(R.color.bluegrass));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //canvas.drawLine(0,0, getWidth(), getHeight(), mLinePaint);


        Log.d(TAG, "" + getWidth());
        canvas.drawRect(map, mBackgroundPaint);

        int boxWidth = (map.width() - (4 * STROKE_WIDTH))/5;

        for (int i = 0; i < 4; i++){
            int x = map.left + ((i + 1) * boxWidth) + ((i) * STROKE_WIDTH);
            canvas.drawLine(x, map.top, x, map.bottom, mLinePaint);
        }

        for (int i = 0; i < 4; i++){
            int y = map.top + ((i + 1) * boxWidth) + ((i) * STROKE_WIDTH);
            canvas.drawLine(map.left, y, map.right, y, mLinePaint);
        }



        canvas.drawPath(mPath, mLinePaint);

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;

        map = new Rect(0,
                getHeight() / 2 - getWidth() / 2,
                getWidth(),
                getHeight() / 2 + getWidth() / 2);
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
