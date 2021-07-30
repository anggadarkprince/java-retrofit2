package com.anggaari.basic.sustainableclient;

import com.anggaari.github.GithubService;
import com.anggaari.github.models.GitHubRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GithubService client = ServiceGenerator.createService(GithubService.class);
        client.reposForUser("anggadarkprince").enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    System.out.println(response.body().get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
