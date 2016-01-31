package com.moonbytes.colorwheel.Helper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dasteini on 29.01.16.
 */
public interface DeviceRestService {
    @GET("info")
    Call<DeviceResponse> getDeviceInformation();

    @GET("updateDevice")
    Call<DeviceResponse> updateDevice(
            @Query("color") String color,
            @Query("power") Integer power
    );
}
