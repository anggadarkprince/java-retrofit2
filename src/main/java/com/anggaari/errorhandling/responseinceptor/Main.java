package com.anggaari.errorhandling.responseinceptor;

import com.anggaari.basic.github.GithubService;
import com.anggaari.basic.github.models.GitHubRepo;
import com.anggaari.basic.sustainableclient.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create an instance of our API interface.
        ServiceGenerator.changeApiBaseUrl("https://api.github.com/");
        GithubService service = ServiceGenerator.createService(GithubService.class);
        Call<List<GitHubRepo>> call = service.reposForUser("anggadarkprince");
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                if (response.isSuccessful()) {
                    System.out.println("server returned so many repositories: " + response.body().size());
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            System.out.println("not found");
                            break;
                        case 500:
                            System.out.println("server broken");
                            break;
                        default:
                            System.out.println("unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                if (t instanceof IOException) {
                    // logging probably not necessary
                    System.out.println("network failure :( inform the user and possibly retry");
                }
                else {
                    // todo log to some central bug tracking service
                    System.out.println("conversion issue! big problems :(");
                }
            }
        });
    }
}
