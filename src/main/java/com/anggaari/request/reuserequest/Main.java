package com.anggaari.request.reuserequest;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileDownloadService downloadService = ServiceGenerator.createService(FileDownloadService.class);

        Callback<ResponseBody> downloadCallback = new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("server contacted and has file");
                    boolean result = writeResponseBodyToDisk(response.body(), "file.png");
                    System.out.println("download result: " + result);
                } else {
                    System.out.println("server contact failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        };

        Call<ResponseBody> originalCall = downloadService.downloadFileWithDynamicUrlSync("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Blank_square.svg/400px-Blank_square.svg.png");
        originalCall.enqueue(downloadCallback);

        // correct reuse:
        Call<ResponseBody> newCall = originalCall.clone();
        newCall.enqueue(downloadCallback);
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            String path = System.getProperty("user.dir");
            System.out.println("Stored in " + path);
            if (fileName == null) {
                fileName = "file";
            }
            File file = new File(System.getProperty("user.dir") + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    System.out.println("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
