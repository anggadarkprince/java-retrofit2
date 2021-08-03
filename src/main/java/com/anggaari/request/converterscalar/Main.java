package com.anggaari.request.converterscalar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://your.base.url/")
                .build();

        ScalarService service = retrofit.create(ScalarService.class);

        String body = "plain text request body";
        Call<String> call = service.getStringScalar(body);

        // primitive type
        Response<String> response = call.execute();
        String value = response.body();
        System.out.println(value);

        // request body
        String text = "plain text request body";
        RequestBody body2 = RequestBody.create(text, MediaType.parse("text/plain"));

        Call<ResponseBody> call2 = service.getStringRequestBody(body2);
        Response<ResponseBody> response2 = call2.execute();
        String value2 = response2.body().string();
        System.out.println(value2);
    }
}
