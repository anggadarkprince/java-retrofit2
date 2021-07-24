package com.anggaari.api;

import com.anggaari.api.models.users.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> register(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("password_confirmation") String passwordConfirmation);
}
