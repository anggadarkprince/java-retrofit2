package com.anggaari.uploaddownload.downloadstream;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.uploaddownload.download.FileDownloadService;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main {
    public static void main(String[] args) {
        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        FileDownloadService downloadService = ServiceGenerator.createService(FileDownloadService.class);

        String fileUrl = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1280_10MG.mp4";
        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync(fileUrl);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("server contacted and has file");

                    boolean writtenToDisk = com.anggaari.request.reuserequest.Main.writeResponseBodyToDisk(response.body(), "file.mp4");
                    System.out.println("file download was a success? " + writtenToDisk);

                    /* android run on background
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = com.anggaari.request.reuserequest.Main.writeResponseBodyToDisk(response.body());

                            System.out.println("file download was a success? " + writtenToDisk);
                            return null;
                        }
                    }.execute();
                    */
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
