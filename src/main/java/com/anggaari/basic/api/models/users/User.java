package com.anggaari.basic.api.models.users;

public class User {
    public final int id;
    public final String name;
    public final String username;
    public final String email;

    public User(int id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }
}
