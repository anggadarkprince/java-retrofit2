package com.anggaari.request.synchasync;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface TaskService {
    @GET("todos")
    Call<List<Todo>> getTodos();

    @POST("todos")
    Call<Todo> createTodo(@Body Todo task);
}
