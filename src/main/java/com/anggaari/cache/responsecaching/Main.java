package com.anggaari.cache.responsecaching;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(new File("build/tmp/cache/"), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        PhotoService service = retrofit.create(PhotoService.class);
        service.getPhotos().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    response.body().forEach(photo -> {
                        System.out.println(photo.title + " (" + photo.url + ")");

                        try {
                            Photo photo2 = service.getPhoto(photo.id).execute().body();
                            if (photo2 != null) {
                                System.out.println("Get single photo: ID " + photo2.id + " (" + photo2.url + ")");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }
}
