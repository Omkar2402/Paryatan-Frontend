package com.example.otpverify.VerifyEmail.Presenter;

public interface VerifyEmailContract {

    interface Model {
        interface OnFinishedListener {
            void onFinished(VerifyEmailResponse verifyEmailResponse);

            void onFailure(String errStr);
        }

        void getotp(OnFinishedListener onFinishedListener, String email);

    }

    interface View {

        void getotp(VerifyEmailResponse verifyEmailResponse);

        void onResponseFailure(String errStr);

    }

    interface Presenter {

        void onDestroy();

        void requestotp(String email);

    }

}
