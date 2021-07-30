package com.anggaari.basic.multipleinstance;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main {
    public static void main(String[] args) {
        // Shared http client to improve performance for multiple client
        OkHttpClient baseOkHttpClient = new OkHttpClient();
        GsonConverterFactory gsonFactory = GsonConverterFactory.create();

        Retrofit retrofitApiV1 = new Retrofit.Builder()
                .baseUrl("https://futurestud.io/v1/")
                .client(baseOkHttpClient)
                .addConverterFactory(gsonFactory)
                .build();

        // customize client for second Retrofit instance for API v2
        OkHttpClient okHttpClientV2 = baseOkHttpClient
                .newBuilder()
                //.addInterceptor(...)
                //.addInterceptor(...)
                .build();

        Retrofit retrofitApiV2 = new Retrofit.Builder()
                .baseUrl("https://futurestud.io/v2/")
                .client(okHttpClientV2)
                .addConverterFactory(gsonFactory)
                .build();
    }
}
