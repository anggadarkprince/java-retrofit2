package com.anggaari.request.synchasync;

public class Todo {
    public int userId;
    public int id;
    public String title;
    public Boolean completed;

    public Todo(int userId, int id, String title, Boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }
}
