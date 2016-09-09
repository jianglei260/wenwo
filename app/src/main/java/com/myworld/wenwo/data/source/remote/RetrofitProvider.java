package com.myworld.wenwo.data.source.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jianglei on 16/7/11.
 */

public class RetrofitProvider {
    public static final String BASE_URl = "http://www.wenwobei.com";
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new ApiTypeAdapterFactory("askDetail", "data"))
            .create();

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URl).addConverterFactory(new CustomConverterFactory(gson)).build();
        return retrofit;
    }

    public static Retrofit create(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(new CustomConverterFactory(gson)).build();
        return retrofit;
    }
}
