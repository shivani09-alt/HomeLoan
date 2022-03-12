package com.example.homeloan;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({
            "Accept:application/json",
            "api-token:20U9o378o-_TpTvQcZuUvVEJEHNKVdp3wcWsa5Bk7IbpLjlF4ryPASoTxspEdvG8B2Q",
            "user-email:shivjain1999@gmail.com"
    })

    @GET("getaccesstoken")
    Call<JsonObject> getaccesstoken();

//    @Headers({
//            "Accept:application/json",
//            "Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJzaGl2amFpbjE5OTlAZ21haWwuY29tIiwiYXBpX3Rva2VuIjoiMjBVOW8zNzhvLV9UcFR2UWNadVV2VkVKRUhOS1ZkcDN3Y1dzYTVCazdJYnBMamxGNHJ5UEFTb1R4c3BFZHZHOEIyUSJ9LCJleHAiOjE2NDY5MjQ1MTB9.usKGCnNbJISCePMV3E0OVW4C-vbTM33XZHv0HX3uTpU"
//    })

    @GET("states/India")
    Call<JsonArray> getStateList(@Header("Authorization")String Authorization,@Header("Accept") String Accept);
//    @Headers({
//            "Accept:application/json",
//            "Authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJzaGl2amFpbjE5OTlAZ21haWwuY29tIiwiYXBpX3Rva2VuIjoiMjBVOW8zNzhvLV9UcFR2UWNadVV2VkVKRUhOS1ZkcDN3Y1dzYTVCazdJYnBMamxGNHJ5UEFTb1R4c3BFZHZHOEIyUSJ9LCJleHAiOjE2NDY5MjQ1MTB9.usKGCnNbJISCePMV3E0OVW4C-vbTM33XZHv0HX3uTpU"
//    })

    @GET
    Call<JsonArray> getCityList(@Url String url,@Header("Authorization")String Authorization,@Header("Accept") String Accept);

}
