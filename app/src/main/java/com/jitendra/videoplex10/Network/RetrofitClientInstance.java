package com.jitendra.videoplex10.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    public static final String BASE_URL = "https://";
    public static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
