package com.example.otpverify.VerifyEmail.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otpverify.MainActivity;
import com.example.otpverify.R;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailContract;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailPresenter;
import com.example.otpverify.VerifyEmail.Presenter.VerifyEmailResponse;
import com.google.gson.Gson;

public class verify extends AppCompatActivity implements VerifyEmailContract.View{

    EditText inputnumber1, inputnumber2, inputnumber3, inputnumber4, inputnumber5, inputnumber6;
    VerifyEmailResponse verifyEmailResponse;
    TextView showemail;
    VerifyEmailPresenter verifyEmailPresenter;
    String getVerifyEmailResponse;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify);

        verifyEmailPresenter = new VerifyEmailPresenter(this);

        Button submitbuttononclick = findViewById(R.id.submitotp);

        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2 = findViewById(R.id.inputotp2);
        inputnumber3 = findViewById(R.id.inputotp3);
        inputnumber4 = findViewById(R.id.inputotp4);
        inputnumber5 = findViewById(R.id.inputotp5);
        inputnumber6 = findViewById(R.id.inputotp6);

        getVerifyEmailResponse = getIntent().getStringExtra("verify_email_response");
        gson = new Gson();
        verifyEmailResponse = gson.fromJson(getVerifyEmailResponse,VerifyEmailResponse.class);

        //resendotp();

        TextView textView = findViewById(R.id.showemail);
        textView.setText(getIntent().getStringExtra("email"));

        TextView resend = findViewById(R.id.resend_otp);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Resend OTP", "onClick: ");
                getVerifyEmailResponse = getIntent().getStringExtra("verify_email_response");
                //gson = new Gson();

                verifyEmailResponse = gson.fromJson(getVerifyEmailResponse,VerifyEmailResponse.class);
                Toast.makeText(verify.this, verifyEmailResponse.getOtp(), Toast.LENGTH_SHORT).show();
                verifyEmailPresenter.requestotp(getIntent().getStringExtra("email"));
            }
        });

        submitbuttononclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty() && !inputnumber3.getText().toString().trim().isEmpty() && !inputnumber4.getText().toString().trim().isEmpty() && !inputnumber5.getText().toString().trim().isEmpty() && !inputnumber6.getText().toString().trim().isEmpty()) {
                    String otpentered = inputnumber1.getText().toString()+inputnumber2.getText().toString()+inputnumber3.getText().toString()+inputnumber4.getText().toString()+inputnumber5.getText().toString()+inputnumber6.getText().toString();
                    if (otpentered.equals(verifyEmailResponse.getOtp())) {
                        Toast.makeText(verify.this, "OTP verified", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(verify.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(verify.this,"Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(verify.this, "Please enter 6 digit otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        otpnumbermove();



    }

    private void resendotp() {

    }

    public void getotp(VerifyEmailResponse verifyEmailResponse) {

    }


    public void onResponseFailure(String errStr) {

    }

    private void otpnumbermove() {

        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputnumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}