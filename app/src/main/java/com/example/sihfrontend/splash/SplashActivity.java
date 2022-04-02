package com.example.sihfrontend.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sihfrontend.MainActivity;
import com.example.sihfrontend.OTPVerification;
import com.example.sihfrontend.R;
import com.example.sihfrontend.admin.AdminHomeActivity;
import com.example.sihfrontend.admin.AdminMainActivity;
import com.example.sihfrontend.helper.VideoHelper;
import com.example.sihfrontend.register.RegisterActivity;
import com.example.sihfrontend.user.UserMainActivity;
import com.example.sihfrontend.user.monument.MonumentDescription;
import com.example.sihfrontend.user.ticket.MonumentBookTickets;
import com.example.sihfrontend.user.ticket.TicketQR;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        Color.parseColor("#e6f9ff");
        Log.d("Color", String.valueOf(Color.parseColor("#e6f9ff")));

        VideoView videoView = findViewById(R.id.video_view);
        try {
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(videoView);
//            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                    R.raw.splash_video));
            videoView.start();
        }catch (Exception e){
            e.printStackTrace();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //////////////////////////
                SharedPreferences sh = SplashActivity.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);
                String  role = sh.getString("role",null);
                String token = sh.getString("token",null);
                Log.d("token",""+token);
                Log.d("role",""+role);
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(getString(R.string.api)+"/refresh-token")
                            .addHeader("Authorization", "Bearer "+token)
                            .addHeader("isRefreshToken", "true")
                            .get()
                            .build();

                    Log.d("Before Response",request.toString());

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String message = "";
                            if(response.isSuccessful()){
                                try {

                                    SharedPreferences.Editor editor = sh.edit();
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    message = jsonObject.getString("message");
                                    String token = jsonObject.getString("token");
                                    Log.d("message:",message);
                                    Log.d("token:",token);
                                    editor.putString("token",token);
                                    editor.apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                                String finalMessage = message;
                                SplashActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(finalMessage.equals("Token refreshed successfully!!")){
                                            if(role.equals("user")){
                                                Intent verifyIntent = new Intent(getApplicationContext(), UserMainActivity.class);
                                                startActivity(verifyIntent);
                                            }else{
                                                Intent verifyIntent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                                startActivity(verifyIntent);
                                            }
                                        }
                                        else{
                                            Toast.makeText(SplashActivity.this,"Token not refreshed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(role!=null && token!=null){


                    if(role.equals("user")){
                        Intent intent = new Intent(SplashActivity.this, UserMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
                        String Information = sharedPreferences.getString("Information",null);
                        Log.d("Information",""+Information);
                        if (Information!=null) {
                            Intent intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                            finish();
                        }

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
        }, 3100);
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