package com.anggaari.request.defaultvalue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedbackService {
    @POST("/feedback")
    Call<ResponseBody> sendFeedbackSimple(@Body UserFeedback body);
}
