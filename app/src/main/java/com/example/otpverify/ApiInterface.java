package com.example.otpverify;

import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("/verify-email")
    Call<VerifyEmailResponse> getUserInformation(@Part("email") RequestBody email);
}
