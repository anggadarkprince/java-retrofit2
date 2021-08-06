package com.anggaari.cache.checkresponseorigin;

import com.anggaari.cache.responsecaching.Photo;
import com.anggaari.cache.responsecaching.PhotoService;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        //File cacheDir = getCacheDir();
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
                    if (response.raw().cacheResponse() != null) {
                        // true: response was served from cache
                        System.out.println("response was served from cache");
                    }

                    if (response.raw().networkResponse() != null) {
                        // true: response was served from network/server
                        System.out.println("response was served from network/server");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
