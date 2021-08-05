package com.anggaari.authentication.token;

import com.anggaari.authentication.basic.LoginService;
import com.anggaari.basic.api.models.users.User;
import com.anggaari.basic.sustainableclient.ServiceGenerator;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        ServiceGenerator.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .build();

            return chain.proceed(request);
        });
        LoginService loginService = ServiceGenerator.createService(LoginService.class);

        Call<Credential> callLogin = loginService.login("angga", "anggaari");
        callLogin.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Credential> call, Response<Credential> response) {
                if (response.isSuccessful()) {
                    System.out.println("Login success");
                    getUserInfo(response.body());
                } else {
                    if (response.code() == 401) {
                        System.out.println("Username or password wrong");
                    } else {
                        System.out.println("Cannot log you in, try again later");
                    }
                }
            }

            @Override
            public void onFailure(Call<Credential> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    public static void getUserInfo(Credential credential) {
        LoginService loginServiceWithCredential = ServiceGenerator.createService(LoginService.class, credential.accessToken);
        Call<User> callMe = loginServiceWithCredential.me();
        try {
            User user = callMe.execute().body();
            System.out.println(user.name);
        } catch (IOException e) {
            System.out.println("Error me: " + e.getMessage());
        }
    }
}
