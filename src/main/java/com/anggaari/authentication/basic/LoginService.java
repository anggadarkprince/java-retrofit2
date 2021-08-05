package com.anggaari.authentication.basic;

import com.anggaari.authentication.token.Credential;
import com.anggaari.basic.api.models.users.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @POST("login/basic")
    Call<User> basicLogin();

    @FormUrlEncoded
    @POST("login/login")
    Call<Credential> login(@Field("username") String username, @Field("password") String password);

    @POST("login/me")
    Call<User> me();
}
