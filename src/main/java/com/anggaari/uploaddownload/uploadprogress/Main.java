package com.anggaari.uploaddownload.uploadprogress;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import com.anggaari.uploaddownload.upload.FileUploadService;
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

        String type = URLConnection.guessContentTypeFromName(file.getName());
        ProgressRequestBody requestFile = new ProgressRequestBody(file, type, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onStart(double percentage, long uploaded, long fileLength) {
                System.out.println("Upload is started");
            }

            @Override
            public void onProgressUpdate(double percentage, long uploaded, long fileLength) {
                System.out.println("Upload progress " + percentage + "% of " + uploaded + "/" + fileLength);
            }

            @Override
            public void onFinish(double percentage, long uploaded, long fileLength) {
                System.out.println("Finish " + percentage + "% of " + uploaded + "/" + fileLength);
            }
        });
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("attachment", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(descriptionString, okhttp3.MultipartBody.FORM);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, filePart);
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
