package com.anggaari.request.requestbody;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.request.synchasync.TaskService;
import com.anggaari.request.synchasync.Todo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main {
    public static void main(String[] args) {
        TaskService taskService = ServiceGenerator.createService(TaskService.class);

        Todo todo = new Todo(1, 1, "Buy a cup of coffee", false);
        Call<Todo> call = taskService.createTodo(todo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(Call<Todo> call, Response<Todo> response) {
                if (response.isSuccessful()) {
                    Todo todo = response.body();
                    System.out.println(todo.title);
                }
            }

            @Override
            public void onFailure(Call<Todo> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
