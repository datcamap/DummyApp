package com.example.dummyapp.data.remote;


import com.example.dummyapp.data.model.AquaDeviceTwin;
import com.example.dummyapp.data.model.SensorsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;

public interface AquaService {
    @Headers({"Accept: application/json"})
    @GET("devices/7")
    Call<AquaDeviceTwin> getAnswers();
    @Headers({"Accept: application/json"})
    @GET("sensordatas?deviceId[eq]=7&latest=true")
    Call<SensorsData> getLastestSensor();
    @PATCH("/devices/7")
    Call<AquaDeviceTwin> patchRequest();
}
