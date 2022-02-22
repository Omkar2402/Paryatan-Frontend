package com.example.otpverify.VerifyEmail.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.otpverify.R;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailContract;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailPresenter;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailResponse;
import com.google.gson.Gson;

public class EnteremailActivity extends AppCompatActivity implements VerifyEmailContract.View {

//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://localhost:8080/verify-email")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();

    EditText input_email;
//    EditText verifybtn;
    VerifyEmailPresenter verifyEmailPresenter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        verifyEmailPresenter = new VerifyEmailPresenter(this);
        input_email = findViewById(R.id.input_email);
        Button verifybtn = findViewById(R.id.verify_email);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input_email.getText().toString().trim().isEmpty()) {
                    verifyEmailPresenter.requestotp(input_email.getText().toString().trim());

                }
                else {
                    Toast.makeText(EnteremailActivity.this, "Enter email id", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void getotp(VerifyEmailResponse verifyEmailResponse) {
        Intent intent = new Intent(getApplicationContext(),verify.class);
        intent.putExtra("email",input_email.getText().toString());
        String arrayAsString = new Gson().toJson(verifyEmailResponse);
        intent.putExtra("verify_email_response",arrayAsString);
        startActivity(intent);
    }

    @Override
    public void onResponseFailure(String errStr) {

    }
//    private void verifybtnSendPostReqclicked() {
//        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
//        Call<VerifyEmailResponse> call = apiInterface.getUserInformation();
//        call.enqueue(new Callback<VerifyEmailResponse>() {
//            @Override
//            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
//                Log.e(TAG, "onResponse: "+response.code());
//                Log.e(TAG, "onResponse: message: "+response.body().getMessage());
//                Log.e(TAG, "onResponse: otp: "+response.body().getOtp());
//
//            }
//
//            @Override
//            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: "+t.getMessage());
//            }
//        });
//    }
}



//    final EditText input_email = findViewById(R.id.input_email);
//        Button verifybtn = findViewById(R.id.verify_email);
//
//        verifybtn.setOnClickListener(view -> {
//
//            if (!enteremail.getText().toString().trim().isEmpty()) {
//                Intent intent = new Intent(getApplicationContext(),verify.class);
//                intent.putExtra("email",enteremail.getText().toString());
//                startActivity(intent);
//            }
//            else {
//                Toast.makeText(main.this, "Enter email id", Toast.LENGTH_SHORT).show();
//            }
//
//        });


