package com.moonbytes.colorwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dasteini on 26.01.16.
 */
public class ColorPickerView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private boolean drawing = true;
    private int HUE_HEIGHT = 150;
    private Paint p;
    private int width, height;
    private float stretchFactor;
    private float satStretchFactor;
    private float hue = 60, saturation = 1.0f, value = 1.0f;
    private List<ColorChangeListener> colorChangeListeners;

    private Thread drawThread;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
        colorChangeListeners = new ArrayList<ColorChangeListener>();
        setOnTouchListener(this);
        init();
    }

    public void onResumeView() {
        drawThread = new Thread(this);
        drawThread.start();
    }

    private void drawHue() {
        stretchFactor = 360.0f/width;
        satStretchFactor = 1.0f / width;

        int lineIndex = 0;

        width = canvas.getWidth();
        height = canvas.getHeight();
        Log.d("View", "W/H: " + width + "/" + height);


        // Draw hue curve
        for(float i = 0; i < 360.0f; i+=stretchFactor) {
            float[] hsv = {i,1.0f,1.0f};
            p.setColor(Color.HSVToColor(hsv));
            canvas.drawLine(lineIndex, 0, lineIndex, HUE_HEIGHT, p);
            lineIndex++;
        }


    }

    private void drawSVView() {
        float pSaturation = 0.0f, pValue = 0.0f;
        for(int x = 0; x < width; x++) {
            for(int y = HUE_HEIGHT; y < width+HUE_HEIGHT; y++) {
                float[] hsv = {hue, pSaturation, pValue};
                p.setColor(Color.HSVToColor(hsv));
                canvas.drawPoint(x,y,p);
                pSaturation += satStretchFactor;
            }
            pSaturation = 0.0f;
            pValue += satStretchFactor;
        }
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


        setMeasuredDimension(widthSize,widthSize+HUE_HEIGHT);
    }


    private void init() {
        // Inititalize paint
        p = new Paint();
        p.setAntiAlias(false);





    }

    @Override
    public void run() {
        while(true) {
            if(surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();

                canvas.drawColor(Color.BLACK);
                drawHue();
                drawSVView();
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
/*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        }



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
