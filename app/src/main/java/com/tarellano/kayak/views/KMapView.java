package com.tarellano.kayak.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Thomas Arellano on 7/2/2016.
 */
public class KMapView extends View {

    private Paint mBackgroundPaint;

    public KMapView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mBackgroundPaint = new Paint(0);
        mBackgroundPaint.setColor(0xff101010);
        mBackgroundPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

    }

    @Override
    public void onDraw(Canvas canvas){

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;
    }
}
