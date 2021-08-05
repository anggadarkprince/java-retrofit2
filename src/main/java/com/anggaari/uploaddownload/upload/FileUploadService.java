package com.anggaari.uploaddownload.upload;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

import java.util.List;
import java.util.Map;

public interface FileUploadService {
    @Multipart
    @POST("upload/store")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    @Multipart
    @POST("upload/store")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("upload/store")
    Call<ResponseBody> uploadMultipleFiles(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2
    );

    @Multipart
    @POST("upload/documents")
    Call<ResponseBody> uploadMultipleFilesDynamic(
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST("upload/store")
    Call<ResponseBody> uploadFileWithPartMap(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file
    );
}
