package com.moonbytes.colorwheel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moonbytes.colorwheel.Helper.DeviceDatabase;
import com.moonbytes.colorwheel.Helper.DeviceItem;

/**
 * This Activity manages the adding of a new DeviceItem to the database
 */

public class EditDeviceActivity extends AppCompatActivity {
    // Views
    private TextView ipAddress, deviceName;
    private Button cancelBtn, addBtn;

    // Booleans to check if the entered IP address is valid
    private boolean ipValid = false, nameValid = false;

    // Database reference
    private DeviceDatabase deviceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Initialize database and UI
        initDB();
        initUI();
    }

    private void initDB() {
        // Initialize device database
        deviceDatabase = new DeviceDatabase(getApplication());
    }

    private void initUI() {
        ipAddress = (TextView) findViewById(R.id.ipAddress);
        deviceName = (TextView) findViewById(R.id.deviceName);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        // By default, the add button is disabled to avoid enter empty devices
        addBtn.setActivated(false);

        // Add two text changed listeners
        ipAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ipValid = checkValidAddress(s.toString());
                checkAddButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    nameValid = true;
                } else nameValid = false;
                checkAddButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Add a new device to the database after checking if device is abailable
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceItem d = getDeviceFromInput();
                deviceDatabase.addDevice(d);
                // TODO check if device is online
                Toast.makeText(getApplicationContext(), "Added device! " + d.getDeviceName(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Cancel add new device and close activity
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
     * @return DeviceItem object generated from input text fields
     */
    private DeviceItem getDeviceFromInput() {
        DeviceItem d = new DeviceItem();
        d.setDeviceName(deviceName.getText().toString());
        d.setIpAddress(ipAddress.getText().toString());
        return d;
    }

    // Checks if we can enable the add button
    private void checkAddButton() {
        addBtn.setActivated(ipValid && nameValid);
    }

    /**
     * Checks, if an IP address has the right format, e.g. 192.168.0.1
     * @param s IP address as String
     * @return valid or not valid
     */
    private boolean checkValidAddress(String s) {
        int points = 0;
        String[] parts = s.split("[.]");

        for(String p : parts) {
            try {
                Log.d("App", p);
                int i = Integer.parseInt(p);
                if(i >= 0 && i <= 255) points++;

            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return points == 4;
    }

}
