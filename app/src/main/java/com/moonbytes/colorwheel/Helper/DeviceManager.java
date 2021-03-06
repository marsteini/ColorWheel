package com.moonbytes.colorwheel.Helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * DeviceManager has the ability to check each device from the database if its online, offline or has
 * some other state. This class is using Retrofit 2 framework to do communicate with the REST API.
 */
public class DeviceManager {
    private List<DeviceItem> devices;
    private Context context;
    private List<DeviceRestListener> restListeners;

    public DeviceManager(Context context, List<DeviceItem> devices) {
        this.devices = devices;
        this.context = context;
        restListeners = new ArrayList<DeviceRestListener>();
    }

    public void setOnDeviceCheckedListener(DeviceRestListener l) {
        restListeners.add(l);
    }

    public void checkDevices() {
        for(DeviceItem d : devices) {
            checkDevice(d);
        }
        devices.clear();
    }

    public void checkDevice(final DeviceItem device) {
        // Building the retrofit interface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+device.getIpAdress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DeviceRestService deviceService = retrofit.create(DeviceRestService.class);

        Call<DeviceResponse> call = deviceService.getDeviceInformation();

        // Do an asynchronous call to the API and notify the DeviceRestListener interface when request
        // is done
        call.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(Response<DeviceResponse> response) {
                if (response == null) {
                    device.setStatus(DeviceItem.STATUS_NO_IOT);
                }
                for (DeviceRestListener d : restListeners) {
                    d.onDeviceChecked(device);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                device.setStatus(DeviceItem.STATUS_OFFLINE);
                for (DeviceRestListener l : restListeners) {
                    l.onDeviceChecked(device);
                }
            }
        });
    }

    public void updateDevice(DeviceItem device) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+device.getIpAdress())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DeviceRestService deviceService = retrofit.create(DeviceRestService.class);
        String hexColor = device.getColor().toString();
        int power;
        if(device.isPoweredOn()) power = 1;
        else power = 0;
        Call<DeviceResponse> call = deviceService.updateDevice(hexColor, power);

        Log.d("Rest", hexColor);

        call.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(Response<DeviceResponse> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    // Notifies the activity when an device was checked
    public interface DeviceRestListener {
        void onDeviceChecked(DeviceItem deviceItem);
    }
}
