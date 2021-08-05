package com.anggaari.authentication.refreshtoken;

import com.anggaari.authentication.refreshtoken.services.TokenService;
import org.jetbrains.annotations.Nullable;

public class TokenServiceHolder {
    TokenService tokenService = null;

    @Nullable
    public TokenService get() {
        return tokenService;
    }

    public void set(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}
