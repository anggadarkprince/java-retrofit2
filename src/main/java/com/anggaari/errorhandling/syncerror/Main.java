package com.anggaari.errorhandling.syncerror;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.request.queryparam.Post;
import com.anggaari.request.queryparam.PostService;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<List<Post>> call = service.getPosts(1);
        try {
            List<Post> posts = call.execute().body();
            posts.forEach(post -> System.out.println(post.title));
        } catch (IOException e) {
            System.out.println("network failure :( - " + e.getMessage());
        }
    }
}
