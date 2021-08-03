package com.anggaari.request.queryparam;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import retrofit2.Call;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<List<Post>> call = service.getPosts(1);
        List<Post> posts = call.execute().body();
        if (posts != null) {
            posts.forEach(post -> System.out.println(post.title));
        }

        System.out.println("--------------------------");

        Map<String, String> data = new HashMap<>();
        data.put("userId", "1");
        data.put("page", String.valueOf(2));
        data.put("isPublished", String.valueOf(true));
        Call<List<Post>> call2 = service.getPosts(data);
        List<Post> posts2 = call2.execute().body();
        if (posts2 != null) {
            posts2.forEach(post -> System.out.println(post.title));
        }
    }
}
