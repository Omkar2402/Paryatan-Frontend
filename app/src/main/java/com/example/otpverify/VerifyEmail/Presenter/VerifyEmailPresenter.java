package com.example.otpverify.VerifyEmail.Presenter;


import com.example.otpverify.VerifyEmail.Service.VerifyEmailService;

public class VerifyEmailPresenter implements VerifyEmailContract.Model.OnFinishedListener, VerifyEmailContract.Presenter {
    VerifyEmailContract.Model model;
    VerifyEmailContract.View view;

    public VerifyEmailPresenter(VerifyEmailContract.View view) {
        this.view = view;
        this.model = new VerifyEmailService();
    }

    @Override
    public void onFinished(VerifyEmailResponse verifyEmailResponse) {
        view.getotp(verifyEmailResponse);
    }

    @Override
    public void onFailure(String errStr) {
        view.onResponseFailure(errStr);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void requestotp(String email) {
        model.getotp(this, email);
    }
}
