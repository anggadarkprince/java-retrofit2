package com.anggaari.api.models.users;

public class UserResponse {
    public final String status;
    public final int code;
    public final UserData data;

    public UserResponse(String status, int code, UserData data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }
}
