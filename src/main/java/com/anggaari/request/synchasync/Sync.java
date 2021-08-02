package com.anggaari.request.synchasync;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public class Sync {
    public static void main(String[] args) throws IOException {
        ServiceGenerator.changeApiBaseUrl("https://jsonplaceholder.typicode.com/");
        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<List<Todo>> call = taskService.getTodos();
        List<Todo> tasks = call.execute().body();
        System.out.println(tasks.get(0).title);
    }
}
