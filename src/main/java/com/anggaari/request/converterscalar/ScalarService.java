package com.anggaari.request.converterscalar;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ScalarService {
    @POST("path")
    Call<String> getStringScalar(@Body String body);

    @POST("path")
    Call<ResponseBody> getStringRequestBody(@Body RequestBody body);
}
