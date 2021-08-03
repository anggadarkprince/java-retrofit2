package com.anggaari.request.fieldmap;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.request.queryparam.Post;
import retrofit2.Call;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        PostService service = ServiceGenerator.createService(PostService.class);

        Map<String, String> postData = Map.of(
                "userId", "1",
                "title", "New post",
                "body", "This is a post"
        );
        Call<Post> call = service.create(postData);
        Post post = call.execute().body();
        if (post != null) {
            System.out.println(post.title);
            System.out.println(post.body);
        } else {
            System.out.println("No result");
        }
    }
}
