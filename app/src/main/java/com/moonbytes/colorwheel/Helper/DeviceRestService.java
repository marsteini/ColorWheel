package com.moonbytes.colorwheel.Helper;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dasteini on 29.01.16.
 */
public interface DeviceRestService {
    @GET("info")
    Call<DeviceResponse> getDeviceInformation();
}
