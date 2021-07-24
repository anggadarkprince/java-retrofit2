package com.anggaari.api.models.errors;

public class ValidationErrorResponse<T> extends ErrorResponse{
    public final T errors;

    public ValidationErrorResponse(String status, int code, String message, T errors) {
        super(status, code, message);
        this.errors = errors;
    }
}
