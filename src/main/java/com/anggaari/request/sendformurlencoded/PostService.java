package com.anggaari.request.sendformurlencoded;

import com.anggaari.request.queryparam.Post;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.List;

public interface PostService {
    @POST("posts")
    @FormUrlEncoded
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("content") String content
    );

    @POST("posts")
    @FormUrlEncoded
    Call<Post> createPost(
            @Field("userId") List<Integer> userIds,
            @Field("title") String title,
            @Field("content") String content
    );
}
