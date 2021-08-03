package com.anggaari.converter.xml;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET
    Call<Task> getTask();
}
