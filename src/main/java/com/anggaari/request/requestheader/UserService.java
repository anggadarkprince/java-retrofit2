package com.anggaari.request.requestheader;

import com.anggaari.api.models.users.User;
import com.anggaari.request.synchasync.Todo;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    // custom header (override the Interceptor)
    @Headers("Cache-Control: max-age=640000")
    @GET("users")
    Call<List<User>> getUsers();

    // multiple header in object
    @Headers({
            "Accept: application/vnd.yourapi.v1.full+json",
            "User-Agent: Your-App-Name"
    })
    @GET("users/{user_id}")
    Call<User> getUser(@Path("user_id") int userId);

    // dynamic header
    @GET("/todos")
    Call<List<Todo>> getTodos(@Header("Content-Range") String contentRange);

    @GET("/todos/{todo_id}")
    Call<Todo> getTodo(@Path("todo_id") int todoId, @HeaderMap Map<String, String> headers);
}
