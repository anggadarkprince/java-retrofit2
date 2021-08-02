package com.anggaari.request.synchasync;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class Async {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("https://jsonplaceholder.typicode.com/");
        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<List<Todo>> call = taskService.getTodos();

        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(@NotNull Call<List<Todo>> call, @NotNull Response<List<Todo>> response) {// get raw response
                okhttp3.Response raw = response.raw();
                if (response.isSuccessful()) {
                    // todos available
                    List<Todo> todos = response.body();
                    System.out.println("Todo length: " + todos.size());
                    System.out.println("First todo: " + todos.get(0).title);
                } else {
                    // error response, no access to resource?
                    System.out.println("Something went wrong: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Todo>> call, @NotNull Throwable t) {
                // something went completely south (like no internet connection)
                System.out.println(t.getMessage());
            }
        });

    }
}
