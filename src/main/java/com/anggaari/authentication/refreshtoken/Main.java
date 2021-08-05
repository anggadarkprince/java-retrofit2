package com.anggaari.authentication.refreshtoken;

import com.anggaari.authentication.refreshtoken.services.TokenService;
import com.anggaari.authentication.refreshtoken.services.UserService;
import com.anggaari.basic.api.models.users.User;
import com.anggaari.basic.sustainableclient.ServiceGenerator;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");

        TokenServiceHolder tokenServiceHolder = new TokenServiceHolder();
        ServiceGenerator.setAuthenticator(new TokenAuthenticator(tokenServiceHolder));
        TokenService tokenService = ServiceGenerator.createService(TokenService.class);
        tokenServiceHolder.set(tokenService);

        UserService userService = ServiceGenerator.createService(UserService.class, AuthData.accessToken);
        Call<User> call = userService.info();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()) {
                    System.out.println("User data: " + response.body().name);
                } else {
                    // this line is not necessary, already handled by TokenAuthenticator
                    if (response.code() == 401) {
                        System.out.println("Invalid credential");
                    } else {
                        System.out.println("Try again later");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
