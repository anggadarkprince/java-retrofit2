package com.anggaari.cache.forceservercachesupport;

import com.anggaari.cache.responsecaching.Photo;
import com.anggaari.cache.responsecaching.PhotoService;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static com.anggaari.authentication.basic.Main.readErrorBody;

public class Main {
    public static void main(String[] args) throws IOException {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(new File("build/tmp/cache/"), cacheSize);

        // Forces cache. Used for cache connection
        Interceptor cacheInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                /*
                 *  If there is no Internet, get the cache that was stored 7 days ago.
                 *  If the cache is older than 7 days, then discard it,
                 *  and indicate an error in fetching the response.
                 *  The 'max-stale' attribute is responsible for this behavior.
                 *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                 */
                Request request = chain.request()
                        .newBuilder()
                        .header("Cache-Control", "only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                        .build();
                return chain.proceed(request);
            }
        };

        // Skips cache and forces full refresh. Used for service connection
        Interceptor noCacheInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        /*
                         *  If there is Internet, get data from server, no cache at all.
                         */
                        .header("Cache-Control", "no-cache")
                        /*
                         *  If there is Internet, get the cache that was stored 60 seconds ago.
                         *  If the cache is older than 5 seconds, then discard it,
                         *  and indicate an error in fetching the response.
                         *  The 'max-age' attribute is responsible for this behavior.
                         */
                        //.header("Cache-Control", "public, max-age=" + 60)
                        .build();
                return chain.proceed(request);
            }
        };

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(noCacheInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create());

        // network service
        Retrofit retrofit = builder.build();
        PhotoService serviceConnection = retrofit.create(PhotoService.class);

        // cache service
        okHttpClient.interceptors().remove(noCacheInterceptor);
        okHttpClient.addInterceptor(cacheInterceptor);
        Retrofit.Builder builderWithCache = builder.client(okHttpClient.build());
        Retrofit retrofitCache = builderWithCache.build();
        PhotoService cacheConnection = retrofitCache.create(PhotoService.class);

        serviceConnection.getPhotos().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                System.out.println("Service request: " + response.body().size());
                if (response.raw().cacheResponse() != null) {
                    System.out.println("serviceConnection: response was served from cache");
                }

                if (response.raw().networkResponse() != null) {
                    // this line is never executed
                    System.out.println("serviceConnection: response was served from network/server");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {

            }
        });

        cacheConnection.getPhotos().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(retrofit2.Call<List<Photo>> call, retrofit2.Response<List<Photo>> response) {
                System.out.println("Cached request: " + response.body());
                if (response.isSuccessful()) {
                    if (response.raw().cacheResponse() != null) {
                        System.out.println("cacheConnection: response was served from cache");
                    }

                    if (response.raw().networkResponse() != null) {
                        System.out.println("cacheConnection: response was served from network/server");
                    }
                } else {
                    System.out.println(readErrorBody(response.errorBody().byteStream()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Photo>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        // cache operation
        Iterator<String> urlIterator = cache.urls();
        while (urlIterator.hasNext()) {
            if (urlIterator.next().startsWith("https://jsonplaceholder.typicode.com/")) {
                urlIterator.remove();
            }
        }
    }
}
