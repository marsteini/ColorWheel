package com.moonbytes.colorwheel;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.danielnilsson9.colorpickerview.view.*;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;
import com.moonbytes.colorwheel.Helper.DeviceItem;
import com.moonbytes.colorwheel.Helper.DeviceManager;

/**
 * This activity shows a ColorPicker and an On/Off switch for the selected device
 */

public class ColorPickerActivity extends AppCompatActivity {
    private com.github.danielnilsson9.colorpickerview.view.ColorPickerView colorView;
    private CardView colorWidget;
    private DeviceItem device;
    private DeviceManager deviceManager;
    private Switch powerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        // Get action bar
        ActionBar actionBar = getSupportActionBar();

        // Get bundle extra from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            device = new DeviceItem();
            device.setDeviceName(extras.getString("deviceName"));
            device.setIpAddress(extras.getString("deviceIp"));
            device.setPoweredOn(extras.getBoolean("power"));

            // Set toolbar title
            actionBar.setTitle(device.getDeviceName());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //
        deviceManager = new DeviceManager(getApplicationContext(),null);

        // Custom colorPicker dialog
        colorView = (com.github.danielnilsson9.colorpickerview.view.ColorPickerView) findViewById(R.id.colorPicker);
        colorWidget = (CardView) findViewById(R.id.colorCard);
        powerSwitch = (Switch) findViewById(R.id.powerSwitch);
        powerSwitch.setChecked(device.isPoweredOn());
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                device.setPoweredOn(isChecked);
                updateDevice();
            }
        });

        colorView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int newColor) {
                colorWidget.setCardBackgroundColor(newColor);
                updateDevice();
            }
        });
    }

    private void updateDevice() {
        //deviceManager.updateDevice(device);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
