package com.anggaari.request.defaultvalue;

import com.anggaari.basic.sustainableclient.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main {
    public static void main(String[] args) {
        FeedbackService service = ServiceGenerator.createService(FeedbackService.class);
        Call<ResponseBody> call = service.sendFeedbackSimple(new UserFeedback("hello"));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
