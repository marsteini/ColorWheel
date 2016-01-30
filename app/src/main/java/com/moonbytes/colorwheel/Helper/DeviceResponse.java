package com.moonbytes.colorwheel.Helper;

import android.graphics.Color;

/**
 * Created by dasteini on 29.01.16.
 */
public class DeviceResponse {
    private String color;
    private int power;
    private int type;

    public static final int POWER_ON = 1, POWER_OFF = 0,
                            TYPE_RGB = 0, TYPE_SWITCH = 1;

    public int getColor() {
        return Color.parseColor(color);
    }

    String getStringColor() {
        return color;
    }

    public int getPowerStatus() {
        return power;
    }


}
