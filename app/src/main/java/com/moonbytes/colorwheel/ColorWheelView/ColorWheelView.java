package com.moonbytes.colorwheel.ColorWheelView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasteini on 25.01.16.
 */
public class ColorWheelView extends View implements View.OnTouchListener {
    private Paint p;
    private float stretchFactor, satStretchFactor;
    private List<ColorChangeListener> colorChangeListeners;
    final String xmlns="http://schemas.android.com/apk/res/android";
    // Store S (saturation) and V (value) in this class
    private float saturation = 1.0f, value = 1.0f, hue = 0.0f;

    private  int HUE_HEIGHT;

    int width;
    int height;

    public ColorWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        HUE_HEIGHT = attrs.getAttributeIntValue(xmlns,"hueHight", 70);
        colorChangeListeners = new ArrayList<ColorChangeListener>();
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        p = new Paint();
        p.setAntiAlias(true);

        width = canvas.getWidth();
        height = canvas.getHeight();

        // HueHeight
        //HUE_HEIGHT = (int) (height * 0.2);

        stretchFactor = 360.0f/width;
        Log.d("App", "Stretching factor: "+stretchFactor + " Canvas-Size: " + width + "/" + height);

        int lineIndex = 0;

        // Draw hue curve
        for(float i = 0; i < 360.0f; i+=stretchFactor) {
            float[] hsv = {i,1.0f,1.0f};
            p.setColor(Color.HSVToColor(hsv));
            canvas.drawLine(lineIndex, 0, lineIndex, HUE_HEIGHT, p);

            lineIndex++;
        }

        satStretchFactor = 1.0f / width;

        drawSVView(canvas);

    }

    private void drawSVView(Canvas canvas) {
        float pSaturation = 0.0f, pValue = 0.0f;

        for(int x = 0; x < width; x++) {
            for(int y = HUE_HEIGHT; y < width; y++) {
                float[] hsv = {hue, pSaturation, pValue};
                p.setColor(Color.HSVToColor(hsv));
                canvas.drawPoint(x,y,p);
                pSaturation += satStretchFactor;
            }
            pSaturation = 0.0f;
            pValue += satStretchFactor;
        }
    }

    public void setOnColorChangedListener(ColorChangeListener l) {
        colorChangeListeners.add(l);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        String wMode ="";
        if(widthMode == MeasureSpec.EXACTLY) {
            wMode = "Exactly";
        } else if(widthMode == MeasureSpec.AT_MOST) {
            wMode = "At Most";
        } else {
            wMode = "Not defined";
        }

        Log.d("WidthMode", wMode);

        setMeasuredDimension(widthSize,widthSize+HUE_HEIGHT);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if(event.getY() < HUE_HEIGHT) {
            hue = event.getX() * stretchFactor;
            invalidate();
        } else {
            saturation = (event.getY() - HUE_HEIGHT)*satStretchFactor;
            value = event.getX() * satStretchFactor;
        }



        float[] hsv = {hue, saturation, value};

        int color = Color.HSVToColor(hsv);
        for(ColorChangeListener l : colorChangeListeners) {
            l.onColorChanged(color);
        }
        return true;
    }

    public interface ColorChangeListener {
        void onColorChanged(int color);
    }


}
