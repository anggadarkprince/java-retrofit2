package com.anggaari.authentication.token;

import com.google.gson.annotations.SerializedName;

public class Credential {
    public String type;
    @SerializedName("access_token")
    public String accessToken;
}
