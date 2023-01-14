package com.example.dummyapp.data.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://aqua-iot.pro/api/v1/";
    public static AquaService getAquaService() {
        return RetrofitClient.getClient(BASE_URL).create(AquaService.class);
    }
}
