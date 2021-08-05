package com.anggaari.authentication.refreshtoken.services;

import com.anggaari.authentication.token.Credential;
import com.anggaari.basic.api.models.users.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TokenService {
    @FormUrlEncoded
    @POST("login/refresh")
    Call<Credential> refreshToken(@Field("refresh_token") String refreshToken);

    @GET("login/info")
    Call<User> info();
}
