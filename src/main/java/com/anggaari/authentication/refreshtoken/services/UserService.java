package com.anggaari.authentication.refreshtoken.services;

import com.anggaari.authentication.token.Credential;
import com.anggaari.basic.api.models.users.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @FormUrlEncoded
    @POST("login/login")
    Call<Credential> login(@Field("username") String username, @Field("password") String password);

    @GET("login/info")
    Call<User> info();
}
