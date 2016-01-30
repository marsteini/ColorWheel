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
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
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
    private DeviceManager deviceManager;

    private ProgressBar networkProgress;
    private int dbItemCount, receivedItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
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
        deviceManager.checkDevices();

        deviceManager.setOnDeviceCheckedListener(new DeviceManager.DeviceRestListener() {
            @Override
            public void onDeviceChecked(DeviceItem deviceItem) {
                devices.add(deviceItem);
                dAdapter = new DeviceListAdapter(getApplicationContext(), devices);
                deviceList.setAdapter(dAdapter);
                receivedItemCount++;
                checkProgress();
            }
        });
        devices.clear();
    }

    private void checkProgress() {
        if(dbItemCount == receivedItemCount) {
            networkProgress.setVisibility(View.GONE);
            receivedItemCount = 0;
        }
    }

    private void initUI() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        deviceList = (RecyclerView) findViewById(R.id.deviceList);
        networkProgress = (ProgressBar) findViewById(R.id.progress);

    }

    private void init() {
        networkProgress.setVisibility(View.VISIBLE);
        deviceDb = new DeviceDatabase(this);
        refreshFromDatabase();
        deviceManager = new DeviceManager(this, devices);

        deviceList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(getApplicationContext(), ColorPickerActivity.class);
                startActivity(i);
            }
        }));
        deviceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void refreshFromDatabase() {
        devices = deviceDb.getAllDevices();
        dbItemCount = devices.size();
        dAdapter = new DeviceListAdapter(getApplicationContext(), devices);
        deviceList.setAdapter(dAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshDevices();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshDevices() {

    }
}
