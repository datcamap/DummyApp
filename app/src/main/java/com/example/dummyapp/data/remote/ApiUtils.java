package com.example.dummyapp.data.remote;

public class ApiUtils {
    public static final String BASE_URL = "https://api.stackexchange.com/2.2/";
    public static AquaService getAquaService() {
        return RetrofitClient.getClient(BASE_URL).create(AquaService.class);
    }
}
