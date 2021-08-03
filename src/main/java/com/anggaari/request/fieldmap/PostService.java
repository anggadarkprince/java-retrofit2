package com.anggaari.request.fieldmap;

import com.anggaari.request.queryparam.Post;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface PostService {
    @FormUrlEncoded
    @POST("posts")
    Call<Post> create(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @PUT("posts")
    Call<Post> update(@FieldMap Map<String, String> fields);
}
