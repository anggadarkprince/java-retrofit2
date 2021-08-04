package com.anggaari.uploaddownload.upload;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);

        // wrap actual file with File class
        File file = new File("file.png");

        // create RequestBody instance from file
        String type = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(file, MediaType.parse(type));

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("attachment", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(descriptionString, okhttp3.MultipartBody.FORM);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("upload success");
                } else {
                    System.out.println("upload failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                System.out.println("error: " + t.getMessage());
            }
        });
    }
}
