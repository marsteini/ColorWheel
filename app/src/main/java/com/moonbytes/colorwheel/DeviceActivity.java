package com.moonbytes.colorwheel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.moonbytes.colorwheel.ColorWheelView.ColorWheelView;
import com.moonbytes.colorwheel.Helper.DeviceDatabase;
import com.moonbytes.colorwheel.Helper.DeviceItem;
import com.moonbytes.colorwheel.Helper.DeviceListAdapter;
import com.moonbytes.colorwheel.Helper.DeviceManager;
import com.moonbytes.colorwheel.Helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity {
    private DeviceListAdapter dAdapter;
    private RecyclerView deviceList;
    private DeviceDatabase deviceDb;
    private List<DeviceItem> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        checkRest();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditActivity();
            }
        });
    }

    private void startEditActivity() {
        Intent i = new Intent(this, EditDeviceActivity.class);
        startActivity(i);
    }

    private void deleteDbContent(DeviceDatabase deviceDb) {

        for(DeviceItem d : deviceDb.getAllDevices()) {
            deviceDb.deleteDevice(d);
        }
    }

    private void checkRest() {
        DeviceManager dm = new DeviceManager(this, devices);
        dm.checkDevices();

        dm.setOnDeviceCheckedListener(new DeviceManager.DeviceRestListener() {
            @Override
            public void onDeviceChecked(DeviceItem deviceItem) {
                Log.d("Rest", "Device: " + deviceItem.getDeviceName() + " Status: " + deviceItem.getStatus());
                devices.add(deviceItem);
                dAdapter = new DeviceListAdapter(getApplicationContext(), devices);
                deviceList.setAdapter(dAdapter);
            }
        });
        devices.clear();
    }

    private void init() {
        deviceList = (RecyclerView) findViewById(R.id.deviceList);
        deviceDb = new DeviceDatabase(this);

        deviceList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getApplicationContext(), "Clicked on: " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ColorPickerActivity.class);
                startActivity(i);
            }
        }));
        deviceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshFromDatabase();
    }

    private void refreshFromDatabase() {
        devices = deviceDb.getAllDevices();
        dAdapter = new DeviceListAdapter(getApplicationContext(), devices);
        deviceList.setAdapter(dAdapter);
    }

    private List<DeviceItem> getPseudoDevices() {
        List<DeviceItem> l = new ArrayList<DeviceItem>();
        String[][] names = { {"Steini Zimmer", "Wohnzimmer", "Bad"}, {"192.168.0.12", "192.168.0.23", "192.168.0.3"}};
        for(int i = 0; i < 3; i++) {
            DeviceItem d = new DeviceItem();
            d.setDeviceName(names[0][i]);
            d.setIpAddress(names[1][i]);
            l.add(d);
        }
        return l;
    }


}
