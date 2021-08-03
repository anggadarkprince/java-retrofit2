package com.anggaari.request.sendformurlencoded;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.request.queryparam.Post;
import retrofit2.Call;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PostService service = ServiceGenerator.createService(PostService.class);
        Call<Post> call = service.createPost(1, "Hello", "This is a blog post");
        Post post = call.execute().body();
        if (post != null) {
            System.out.println(post.title);
        } else {
            System.out.println("No result");
        }
    }
}
