package com.anggaari.basic.client;

import com.anggaari.github.GithubService;
import com.anggaari.github.models.GitHubRepo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String API_BASE_URL = "https://api.github.com/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        GithubService client = retrofit.create(GithubService.class);

        // Fetch a list of the Github repositories.
        Call<List<GitHubRepo>> call = client.reposForUser("anggadarkprince");

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isEmpty()) {
                        System.out.println(response.body().get(0).getName());
                        System.out.println(response.body().get(0).getId());
                    }
                } else {
                    System.out.println("Error code: " + response.code());
                    System.out.println("Error message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                System.out.println(t.getMessage());
            }
        });
    }
}
