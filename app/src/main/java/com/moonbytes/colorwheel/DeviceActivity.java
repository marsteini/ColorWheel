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
import com.moonbytes.colorwheel.Helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity {
    private DeviceListAdapter dAdapter;
    private RecyclerView deviceList;
    private DeviceDatabase deviceDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

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

    private void init() {
        deviceList = (RecyclerView) findViewById(R.id.deviceList);

        deviceList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getApplicationContext(), "Clicked on: " + position, Toast.LENGTH_SHORT).show();
            }
        }));
        deviceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshFromDatabase();
    }

    private void refreshFromDatabase() {
        deviceDb = new DeviceDatabase(this);
        dAdapter = new DeviceListAdapter(getApplicationContext(), deviceDb.getAllDevices());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFromDatabase();
    }
}
