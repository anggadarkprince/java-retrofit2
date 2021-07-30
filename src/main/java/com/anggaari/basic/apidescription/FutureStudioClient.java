package com.anggaari.basic.apidescription;

import com.anggaari.basic.apidescription.models.FutureStudioRssFeed;
import com.anggaari.basic.apidescription.models.Tutorial;
import com.anggaari.basic.apidescription.models.UserInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface FutureStudioClient {
    @GET("/user/info")
    Call<UserInfo> getUserInfo();

    @PUT("/user/info")
    Call<UserInfo> updateUserInfo(@Body UserInfo userInfo);

    @DELETE("/user")
    Call<Void> deleteUser();

    // example for passing a full URL
    @GET("https://futurestud.io/tutorials/rss/")
    Call<FutureStudioRssFeed> getRssFeed();

    // dynamic url and get raw response body
    @GET
    Call<ResponseBody> getUserProfilePhoto(@Url String profilePhotoUrl);

    @GET("/{user}/settings")
    Call<UserInfo> getUserSettings(@Path("user") String user);

    @GET("/tutorials")
    Call<List<Tutorial>> getTutorials(@Query("page") Integer page);

    @GET("/tutorials")
    Call<List<Tutorial>> getTutorials(
            @Query("page") Integer page,
            @Query("order") String order,
            @Query("author") String author,
            @Query("published_at") Date date
    );
}
