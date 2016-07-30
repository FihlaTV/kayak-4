package com.tarellano.kayak.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tarellano.kayak.R;

import java.util.Vector;

/**
 * Created by Thomas Arellano on 7/2/2016.
 */
public class KMapView extends View {

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e){
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e){
            float x = e.getX();
            float y = e.getY();

            Point loc = new Point(Math.round(e.getX()), Math.round(e.getY()));

            for (Box box: boxes){
                if (box.getRect().intersect(loc.x, loc.y, loc.x, loc.y)){

                    box.setBackgroundPaint(mAltBoxPaint);

                    return true;
                }
            }


            return true;
        }
    }

    private class Box{

        private Paint mBackgroundPaint;
        private Rect mRect;

        public Box(Rect rect, Paint paint) {
            this.mRect = rect;
            this.mBackgroundPaint = paint;
        }

        public Paint getBackgroundPaint() {
            return mBackgroundPaint;
        }

        public void setBackgroundPaint(Paint mBackgroundPaint) {
            this.mBackgroundPaint = mBackgroundPaint;
        }

        public Rect getRect() {
            return mRect;
        }

        public void setRect(Rect mCoordinates) {
            this.mRect = mCoordinates;
        }
    }

    private GestureDetector mDetector = new GestureDetector(KMapView.this.getContext(), new mListener());

    private final static String TAG = "kmapView";
    private Paint mLinePaint;
    private Paint mBackgroundPaint;
    private Paint mBoxPaint;
    private Paint mAltBoxPaint;
    private static final int STROKE_WIDTH = 8;

    private static final int NUMBER_OF_VARIABLES = 4;
    private Rect map;


    private Vector<Box> boxes;

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
        boxes = new Vector<>(16);

        mBackgroundPaint = new Paint(0);
        mBackgroundPaint.setColor(getResources().getColor(R.color.slate));
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(0);
        mLinePaint.setColor(getResources().getColor(R.color.black));
        mLinePaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(STROKE_WIDTH);

        mBoxPaint = new Paint(0);
        mBoxPaint.setColor(getResources().getColor(R.color.seafoam));
        mBoxPaint.setStyle(Paint.Style.FILL);

        mAltBoxPaint = new Paint(0);
        mAltBoxPaint.setColor(getResources().getColor(R.color.chartreuse));
        mAltBoxPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawRect(map, mBackgroundPaint);

        for (Box box : boxes) {
            canvas.drawRect(box.getRect(), box.getBackgroundPaint());
        }

        canvas.drawPath(mPath, mLinePaint);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;

        Log.d(TAG, "Width: " + ww + "; Height: " + hh);

        map = new Rect(0,
                getHeight() / 2 - getWidth() / 2,
                getWidth(),
                getHeight() / 2 + getWidth() / 2);

        int side = map.width()/5 - STROKE_WIDTH;
        boxes = new Vector<Box>(NUMBER_OF_VARIABLES * NUMBER_OF_VARIABLES);
        for (int i = 0; i < NUMBER_OF_VARIABLES; i++){
            for (int j = 0; j < NUMBER_OF_VARIABLES; j++){
                int left = map.left + side + (i * (side + STROKE_WIDTH));
                int top = map.top + side + (j * (side + STROKE_WIDTH));
                int right = left + side;
                int bottom = top + side;
                //boxes[i + (j * NUMBER_OF_VARIABLES)] = new Rect(left, top, right, bottom);
                boxes.add(j + (i * NUMBER_OF_VARIABLES), new Box(new Rect(left, top, right, bottom), mBoxPaint));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        boolean result = mDetector.onTouchEvent(motionEvent);
        if (!result){
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                result = true;
            }
        }

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
