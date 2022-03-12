package com.example.homeloan;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstances {
    private static RetrofitInstances instance = null;
    private ApiInterface myApi;
    private static String BASE_URL ="https://www.universal-tutorial.com/api/";
    private RetrofitInstances() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitInstances getInstance() {
        if (instance == null) {
            instance = new RetrofitInstances();
        }
        return instance;
    }

    public ApiInterface getMyApi() {
        return myApi;
    }
}
