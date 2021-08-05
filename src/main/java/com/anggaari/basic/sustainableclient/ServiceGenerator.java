package com.anggaari.basic.sustainableclient;

import com.anggaari.authentication.AuthenticationInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;

import java.util.concurrent.TimeUnit;

public class ServiceGenerator {
    public static Boolean isDevelopment = true;
    public static String apiBaseUrl = "https://jsonplaceholder.typicode.com/";

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(JaxbConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(Level.BODY);

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    // No need to instantiate this class.
    private ServiceGenerator() {

    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static Retrofit.Builder builder() {
        return builder;
    }

    public static OkHttpClient.Builder httpClient() {
        return httpClient;
    }

    public static HttpLoggingInterceptor logging() {
        return logging;
    }

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseUrl);
    }

    public static void addInterceptor(Interceptor interceptor) {
        httpClient.addInterceptor(interceptor);
    }

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);

        if (isDevelopment) {
            logging.setLevel(Level.BODY);
        } else {
            logging.setLevel(Level.BASIC);
        }

        if (!httpClient.interceptors().contains(logging) /*&& BuildConfig.DEBUG*/) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        String authToken = Credentials.basic(username, password);
        return createService(serviceClass, authToken);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }

        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor);

            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
