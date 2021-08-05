package com.anggaari.authentication.basic;

import com.anggaari.basic.api.models.users.User;
import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        LoginService loginService = ServiceGenerator.createService(LoginService.class, "admin", "admin123");
        Call<User> call = loginService.basicLogin();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().name);
                } else {
                    // error response, no access to resource?
                    System.out.println(readErrorBody(response.errorBody().byteStream()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                // something went completely south (like no internet connection)
                System.out.println("Error: " + t.getMessage());
            }
        });
    }

    @NotNull
    public static String readErrorBody(InputStream in)
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "";
        }

        return sb.toString();
    }
}
