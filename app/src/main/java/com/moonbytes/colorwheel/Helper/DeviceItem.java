package com.moonbytes.colorwheel.Helper;

import android.graphics.Color;

/**
 * Created by dasteini on 27.01.16.
 */
public class DeviceItem {
    private String IP_ADDRESS, DEVICE_NAME;
    private boolean POWERED_ON, AVAILABLE;
    private Color COLOR;

    public long getId() {
        return ID;
    }

    public void setId(long ID) {
        this.ID = ID;
    }

    private long ID;

    public String getIpAdress() {
        return IP_ADDRESS;
    }

    public void setIpAddress(String IP_ADDRESS) {
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public String getDeviceName() {
        return DEVICE_NAME;
    }

    public void setDeviceName(String DEVICE_NAME) {
        this.DEVICE_NAME = DEVICE_NAME;
    }

    public boolean isPoweredOn() {
        return POWERED_ON;
    }

    public void setPoweredOn(boolean POWERED_ON) {
        this.POWERED_ON = POWERED_ON;
    }

    public boolean isAvailable() {
        return AVAILABLE;
    }

    public void setAvailable(boolean AVAILABLE) {
        this.AVAILABLE = AVAILABLE;
    }

    public Color getColor() {
        return COLOR;
    }

    public void setColor(Color COLOR) {
        this.COLOR = COLOR;
    }
}
