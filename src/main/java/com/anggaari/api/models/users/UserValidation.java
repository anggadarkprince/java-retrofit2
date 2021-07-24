package com.anggaari.api.models.users;

import java.util.List;

public class UserValidation {
    public final List<String> name;
    public final List<String> email;
    public final List<String> password;
    public final List<String> passwordConfirmation;

    public UserValidation(List<String> name, List<String> email, List<String> password, List<String> passwordConfirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
