package com.example.tjgaming.finalproject.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TJ on 10/15/2018.
 */
public class ApiClient {
    public static final String BASE_URL = "https://tvjan-tvmaze-v1.p.mashape.com/";
    public static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
