package com.example.sihfrontend.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sihfrontend.MainActivity;
import com.example.sihfrontend.R;
import com.example.sihfrontend.Test_Image;
import com.example.sihfrontend.register.RegisterActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent splashIntent=new Intent(SplashActivity.this, MainActivity.class);
                //Intent is used to switch from one activity to another.
                Intent splashIntent = new Intent(SplashActivity.this, Test_Image.class);
                startActivity(splashIntent);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, 2500);
    }
}