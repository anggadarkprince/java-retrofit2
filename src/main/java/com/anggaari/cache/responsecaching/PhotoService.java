package com.anggaari.cache.responsecaching;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface PhotoService {
    @GET("photos")
    Call<List<Photo>> getPhotos();

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") int id);
}
