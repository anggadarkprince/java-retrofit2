package com.anggaari.converter.xml;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("API_BASE_URL")
                .client(new OkHttpClient())
                .addConverterFactory(JaxbConverterFactory.create())
                .build();

        TaskService service = retrofit.create(TaskService.class);
        Task task = service.getTask().execute().body();
        System.out.println(task.title);
    }
}
