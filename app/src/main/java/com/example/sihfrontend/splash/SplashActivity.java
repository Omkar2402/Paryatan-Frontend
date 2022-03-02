package com.example.sihfrontend.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.sihfrontend.MainActivity;
import com.example.sihfrontend.R;
import com.example.sihfrontend.admin.AdminMainActivity;
import com.example.sihfrontend.user.UserMainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sh = SplashActivity.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);
                String  role = sh.getString("role",null);
                String token = sh.getString("token",null);
                Log.d("token",""+token);
                Log.d("role",""+role);
                if(role!=null && token!=null){


                    if(role.equals("user")){
                        Intent intent = new Intent(SplashActivity.this, UserMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent splashIntent=new Intent(SplashActivity.this, MainActivity.class);
                    //Intent is used to switch from one activity to another.
//                Intent splashIntent = new Intent(SplashActivity.this, Test_Image.class);
                    startActivity(splashIntent);
                    //invoke the SecondActivity.

                    finish();
                }

                //the current activity will get finished.
            }
        }, 2500);
    }
    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again

//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sh = SplashActivity.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);
//        String  role = sh.getString("role",null);
//        String token = sh.getString("token",null);
//        Log.d("token",""+token);
//        if(role!=null && token!=null){
//            if(role.equals("user")){
//                Intent intent = new Intent(SplashActivity.this, UserMainActivity.class);
//                startActivity(intent);
//                finish();
//            }else{
//                Intent intent = new Intent(SplashActivity.this, AdminMainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//    }
}