package com.anggaari.request.queryparam;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface PostService {
    // posts?userId=1
    @GET("posts")
    Call<List<Post>> getPosts(@Query("userId") long userId);

    // posts?userId=1&userId=2
    // optional parameter set to null
    @GET("posts")
    Call<List<Post>> getPosts(@Query(value = "userId", encoded = true) List<Long> userId, @Query("page") Integer page);

    // posts?userId=1&page=2&isPublished=1 depends on the map
    // optional parameter set to null
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap(encoded = true) Map<String, String> query);
}
