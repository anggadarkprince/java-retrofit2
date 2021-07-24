package com.anggaari.api.models.errors;

public class ErrorResponse {
    public final String status;
    public final int code;
    public final String message;

    public ErrorResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
