package com.example.sihfrontend.payment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sihfrontend.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private EditText amountEdt;
    private Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amountEdt = findViewById(R.id.idEdtAmount);
        payBtn = findViewById(R.id.idBtnPay);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountEdt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter valid amount",Toast.LENGTH_LONG).show();
                }else{
                    String samount = amountEdt.getText().toString();
                    int amount = Math.round(Float.parseFloat(samount)*100);

                    Checkout checkout = new Checkout();

                    checkout.setKeyID("rzp_live_dSG7KmGC5Ox5tS");

                    checkout.setImage(R.drawable.heritage_logo);

                    JSONObject object = new JSONObject();

                    try {

                        // to put name
                        object.put("name", "GOD");

                        // put description
                        object.put("description", "Test payment");

                        // to set theme color
                        object.put("theme.color", "");

                        // put the currency
                        object.put("currency", "INR");

                        // put amount
                        object.put("amount", amount);

                        // put mobile number
                        object.put("prefill.contact", "8237345685");

                        // put email
                        object.put("prefill.email", "shivamvermasv380@gmail.com");

                        // open razorpay to checkout activity
                        checkout.open(PaymentActivity.this, object);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }


            }
        });


    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Payment successfull!!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment failed!!",Toast.LENGTH_LONG).show();
    }
}