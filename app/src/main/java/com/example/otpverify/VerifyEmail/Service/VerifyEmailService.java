package com.example.otpverify.VerifyEmail.Service;

import android.util.Log;

import com.example.otpverify.ApiInterface;
import com.example.otpverify.RetrofitClient;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailContract;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailResponse;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailService implements VerifyEmailContract.Model{
    @Override
    public void getotp(OnFinishedListener onFinishedListener, String email) {
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        final RequestBody requestBodyemail = RequestBody.create(MediaType.parse("text/plain"), email);
        Call<VerifyEmailResponse> verifyEmailResponseCall = apiInterface.getUserInformation(requestBodyemail);
        verifyEmailResponseCall.enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                if (response.isSuccessful()) {
                    VerifyEmailResponse verifyEmailResponse = response.body();
                    if (response.body()==null) {
                        try {
                            onFinishedListener.onFailure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        onFinishedListener.onFinished(verifyEmailResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                Log.e("TAGAPISERVICEerr", t.toString());
                if (t instanceof IOException) {
                    onFinishedListener.onFailure("Failed to connect to server :(");
                    // logging probably not necessary
                } else {
                    onFinishedListener.onFailure(t.getLocalizedMessage());
                    // todo log to some central bug tracking service
                }
            }
        });
    }
}
