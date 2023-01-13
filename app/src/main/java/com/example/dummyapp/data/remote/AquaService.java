package com.example.dummyapp.data.remote;


import com.example.dummyapp.data.model.AquaAnswersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface AquaService {
    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<AquaAnswersResponse> getAnswers();
    @PATCH("/devices/7")
    Call<AquaAnswersResponse> patchRequest();
}