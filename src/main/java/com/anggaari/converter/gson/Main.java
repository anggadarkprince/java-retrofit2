package com.anggaari.converter.gson;

import com.anggaari.basic.github.GithubService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main {
    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubService service = retrofit.create(GithubService.class);
    }
}
