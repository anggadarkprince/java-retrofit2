package com.anggaari.basic.api;

import com.anggaari.basic.api.models.errors.ErrorResponse;
import com.anggaari.basic.api.models.errors.ValidationErrorResponse;
import com.anggaari.basic.api.models.users.UserResponse;
import com.anggaari.basic.api.models.users.UserValidation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello My API");

        // Add http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });

        // Create a very simple REST adapter which points the My API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://angga-ari.local/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        // Create an instance of our API interface.
        ApiService service = retrofit.create(ApiService.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<UserResponse> call = service.register(
                "Angga Arya",
                "angga.aryaa@mail.com",
                "anggaari",
                "anggaari"
        );

        Response<UserResponse> response = call.execute();
        System.out.println("response:" + response);
        System.out.println("response.isSuccessful:" + response.isSuccessful());
        System.out.println("response.code:" + response.code());
        System.out.println("response.errorBody:" + response.errorBody());
        System.out.println("response.headers:" + response.headers());
        System.out.println("response.raw:" + response.raw());
        System.out.println("response.message:" + response.message());

        if (response.isSuccessful()) {
            UserResponse userResponse = response.body();
            System.out.println(new Gson().toJson(userResponse));
            if (userResponse != null) {
                System.out.println("User response:");
                System.out.println("data.accessToken:" + userResponse.data.accessToken);
                System.out.println("data.tokenType:" + userResponse.data.tokenType);
                System.out.println("data.user.name:" + userResponse.data.user.name);
                System.out.println("data.user.email:" + userResponse.data.user.email);
            }
        } else if (response.code() == 422) {
            Type validationType = new TypeToken<ValidationErrorResponse<UserValidation>>() {}.getType();
            ValidationErrorResponse<UserValidation> userValidation = new Gson().fromJson(response.errorBody().charStream(), validationType);

            //Converter<ResponseBody, ValidationErrorResponse<UserValidation>> errorConverter = retrofit.responseBodyConverter(validationType, new Annotation[0]);
            //ValidationErrorResponse<UserValidation> userValidation = errorConverter.convert(response.errorBody());
            System.out.println("Validation error:");
            System.out.println("status:" + userValidation.status);
            System.out.println("code:" + userValidation.code);
            System.out.println("message:" + userValidation.message);
            System.out.println("errors:" + userValidation.errors);
            System.out.println("errors.name:" + userValidation.errors.name);
            System.out.println("errors.email:" + userValidation.errors.email);
            if (userValidation.errors.password!=null) {
                System.out.println("errors.password:" + userValidation.errors.password);
                System.out.println("errors.password:" + userValidation.errors.password.get(0));
            }
            System.out.println("errors.passwordConfirmation:" + userValidation.errors.passwordConfirmation);
        } else if (Arrays.asList(404, 400, 500).contains(response.code())) {
            // List.of(404, 500).contains(response.code());
            // Look up a converter for the Error type on the Retrofit instance.
            Converter<ResponseBody, ErrorResponse> errorConverter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
            // Convert the error body into our Error type.
            if (response.errorBody() != null) {
                ErrorResponse errorResponse = errorConverter.convert(response.errorBody());
                if (errorResponse != null) {
                    System.out.println("Error response body:");
                    System.out.println("status:" + errorResponse.status);
                    System.out.println("code:" + errorResponse.code);
                    System.out.println("message:" + errorResponse.message);
                }
            }
        } else {
            System.out.println("Unknown error");
        }
    }
}
