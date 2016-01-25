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
    private float stretchFactor;
    private List<ColorChangeListener> colorChangeListeners;
    // Store S (saturation) and V (value) in this class
    private float saturation = 1.0f, value = 1.0f;

    public ColorWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorChangeListeners = new ArrayList<ColorChangeListener>();
        setOnTouchListener(this);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        p = new Paint();
        p.setAntiAlias(true);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        stretchFactor = 360.0f/height;
        Log.d("App", "Stretching factor: "+stretchFactor );

        int lineIndex = 0;

        for(float i = 0; i < 360.0f; i+=stretchFactor) {
            float[] hsv = {i,saturation,value};
            p.setColor(Color.HSVToColor(hsv));
            canvas.drawLine(0, lineIndex, width, lineIndex, p);

            lineIndex++;
        }
    }


    public void setOnColorChangedListener(ColorChangeListener l) {
        colorChangeListeners.add(l);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float hue = event.getY() * stretchFactor;
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
