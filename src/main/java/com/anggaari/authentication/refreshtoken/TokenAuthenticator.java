package com.anggaari.authentication.refreshtoken;

import com.anggaari.authentication.refreshtoken.services.TokenService;
import com.anggaari.authentication.token.Credential;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class TokenAuthenticator implements Authenticator {
    private final TokenServiceHolder tokenServiceHolder;

    public TokenAuthenticator(TokenServiceHolder tokenServiceHolder) {
        this.tokenServiceHolder = tokenServiceHolder;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        //is there a TokenService?
        TokenService service = tokenServiceHolder.get();
        if (service == null || response.code() == 400) {
            //there is no way to answer the challenge
            //so return null according to Retrofit's convention
            return null;
        }

        // Refresh your access_token using a synchronous api request
        retrofit2.Call<Credential> credentialResponse = service.refreshToken(AuthData.refreshToken);
        Credential credential = credentialResponse.execute().body();
        if (credential != null) {
            String newAccessToken = credential.accessToken;

            AuthData.accessToken = newAccessToken;

            System.out.println("new access token: " + AuthData.accessToken);

            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .header("Authorization", newAccessToken)
                    .build();
        }
        return null;
    }
}
