package com.anggaari.uploaddownload.downloadprogress;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.uploaddownload.download.FileDownloadService;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        ProgressResponseBody.DownloadCallbacks downloadCallbacks = new ProgressResponseBody.DownloadCallbacks() {
            @Override
            public void onStart(double percentage, long uploaded, long fileLength) {
                System.out.println("Download is started");
            }

            @Override
            public void onProgressUpdate(double percentage, long uploaded, long fileLength) {
                System.out.println("Download progress " + percentage + "% of " + uploaded + "/" + fileLength);
            }

            @Override
            public void onFinish(double percentage, long uploaded, long fileLength) {
                System.out.println("Download finished " + percentage + "%");
            }
        };

        ServiceGenerator.changeApiBaseUrl("http://localhost:8080/");
        ServiceGenerator.addInterceptor(chain -> {
            // return chain.proceed(chain.request());
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), downloadCallbacks))
                    .build();
        });
        FileDownloadService downloadService = ServiceGenerator.createService(FileDownloadService.class);

        String fileUrl = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1280_10MG.mp4";
        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync(fileUrl);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull final Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("server contacted and has file");

                    File file = new File(System.getProperty("user.dir") + File.separator + "file.mp4");
                    byte[] buffer = new byte[4 * 1024];

                    try (InputStream inputStream = response.body().byteStream(); OutputStream outputStream = new FileOutputStream(file)) {
                        while (true) {
                            int read = inputStream.read(buffer);
                            if (read == -1) {
                                break;
                            }
                            outputStream.write(buffer, 0, read);
                        }
                        outputStream.flush();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } finally {
                        System.out.println("Downloaded " + file.getAbsolutePath());
                    }

                    //boolean writtenToDisk = com.anggaari.request.reuserequest.Main.writeResponseBodyToDisk(response.body(), "file.mp4");
                    //System.out.println("file download was a success? " + writtenToDisk);
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
