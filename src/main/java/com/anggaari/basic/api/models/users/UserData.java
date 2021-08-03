package com.anggaari.basic.api.models.users;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("access_token")
    public final String accessToken;
    @SerializedName("token_type")
    public final String tokenType;
    public final User user;

    public UserData(String accessToken, String tokenType, User user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.user = user;
    }
}
