package com.anggaari.request.requestheader;

import com.anggaari.api.models.users.User;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("User-Agent", "My-Application")
                        .header("Accept", "application/json")
                        // add header (no override the existing one)
                        //.addHeader("Accept", "text/html")
                        //.addHeader("Cache-Control", "no-cache, no-store") // same as bellow
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Cache-Control", "no-store")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        UserService service = retrofit.create(UserService.class);
        Call<List<User>> call = service.getUsers();
        List<User> users = call.execute().body();

        if (users != null) {
            users.forEach(user -> System.out.println(user.name));
        }

        // dynamic header must be set every request
        service.getTodos("bytes 200-1000/67589");

        // header map
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Accept-Charset", "utf-8");
        map.put("User-Agent", "My backend app");
        service.getTodo(1, map);
    }
}
