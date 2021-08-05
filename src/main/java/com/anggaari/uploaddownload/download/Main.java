package com.anggaari.uploaddownload.download;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        FileDownloadService downloadService = ServiceGenerator.createService(FileDownloadService.class);

        String fileUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Blank_square.svg/400px-Blank_square.svg.png";
        Call<ResponseBody> call = downloadService.downloadFileWithFixedUrl();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("server contacted and has file");

                    boolean writtenToDisk = com.anggaari.request.reuserequest.Main.writeResponseBodyToDisk(response.body(), "file.png");

                    System.out.println("file download was a success? " + writtenToDisk);
                } else {
                    System.out.println("server contact failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                System.out.println("error: " + t.getMessage());
            }
        });
    }
}
