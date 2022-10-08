package com.app.awaaz.RetrofitController;

import com.app.awaaz.Interfaces.ApiCalls;
import com.app.awaaz.Utils.Constance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.GsonBuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitObject {
    public static RetrofitObject retrofitObject;
    public static Retrofit retrofit;

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    RetrofitObject(){
        retrofit = new Retrofit.Builder().baseUrl(Constance.LOGIN_API).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static synchronized RetrofitObject getInstance(){
        if(retrofitObject == null){
            retrofitObject = new RetrofitObject();
        }
        return retrofitObject;
    }

    public ApiCalls getApi(){
        return retrofit.create(ApiCalls.class);
    }
}
