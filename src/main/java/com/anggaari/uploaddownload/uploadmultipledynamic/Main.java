package com.anggaari.uploaddownload.uploadmultipledynamic;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.uploaddownload.upload.FileUploadService;
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
import java.util.ArrayList;
import java.util.List;

public class Main {
    @NotNull
    private static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(descriptionString, okhttp3.MultipartBody.FORM);
    }

    @NotNull
    private static MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        String type = URLConnection.guessContentTypeFromName(file.getName());
        RequestBody requestFile = RequestBody.create(file, MediaType.parse(type));

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);

        // wrap actual file with File class
        File file1 = new File("file.png");
        File file2 = new File("file.png");
        File file3 = new File("file2.jpg");
        File file4 = new File("unknown.mp4");

        // create list of file parts (photo, video, ...)
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (file1.exists()) parts.add(prepareFilePart("files[]", file1));
        if (file2.exists()) parts.add(prepareFilePart("files[]", file2));
        if (file3.exists()) parts.add(prepareFilePart("files[]", file3));
        if (file4.exists()) parts.add(prepareFilePart("files[]", file4));

        System.out.println(parts.size());
        // add another part within the multipart request
        RequestBody description = createPartFromString("hello, this is description speaking");

        // finally, execute the request
        Call<ResponseBody> call = service.uploadMultipleFilesDynamic(description, parts);
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
