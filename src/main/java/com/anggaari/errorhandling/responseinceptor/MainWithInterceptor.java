package com.anggaari.errorhandling.responseinceptor;

import com.anggaari.basic.github.GithubService;
import com.anggaari.basic.github.models.GitHubRepo;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class MainWithInterceptor {
    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        // todo deal with the issues the way you need to
                        if (response.code() == 500) {
                            System.out.println("internal server error");
                            /*startActivity(
                                    new Intent(
                                            ErrorHandlingActivity.this,
                                            ServerIsBrokenActivity.class
                                    )
                            );*/

                            return response;
                        }

                        return response;
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        GithubService service = retrofit.create(GithubService.class);

        Call<List<GitHubRepo>> call = service.reposForUser("unknown34234");
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, retrofit2.Response<List<GitHubRepo>> response) {
                if (response.isSuccessful()) {
                    System.out.println("server returned so many repositories: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                System.out.println(t.getMessage());
                if (t instanceof IOException) {
                    // logging probably not necessary
                    System.out.println("this is an actual network failure :( inform the user and possibly retry");
                }
                else {
                    // todo log to some central bug tracking service
                    System.out.println("conversion issue! big problems :(");
                }
            }
        });

    }
}
