package com.jitendra.videoplex10.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitVidSearchInstance {
    public static final String Search_URL = "https://www.googleapis.com/youtube/v3/";
    public static Retrofit retrofit = null;

    public static Retrofit getRetrofitVidSearchInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Search_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
