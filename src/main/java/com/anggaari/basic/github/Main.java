package com.anggaari.basic.github;

import com.anggaari.basic.github.models.Contributor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello Retrofit 2");

        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our API interface.
        GithubService service = retrofit.create(GithubService.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = service.contributors("square", "retrofit");

        // Fetch and print a list of the contributors to the library.
        List<Contributor> contributors = call.execute().body();
        if (contributors != null) {
            for (Contributor contributor : contributors) {
                System.out.println(contributor.login + " (" + contributor.contributions + ")");
            }
        }
    }
}
