package com.example.sihfrontend.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sihfrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class InVerificationProcess extends AppCompatActivity {

    ProgressBar progressBar;

    String isVerify = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_verification_process);
        progressBar = findViewById(R.id.progress_bar_verification);
        progressBar.setVisibility(View.VISIBLE);
        isVerified();

    }
    private void isVerified() {
        try{
            SharedPreferences sh = InVerificationProcess.this.getSharedPreferences("SIH",MODE_PRIVATE);
            SharedPreferences sh2 = InVerificationProcess.this.getSharedPreferences("Admin_Monument",MODE_PRIVATE);
            String token = sh.getString("token",null);
            Log.d("token",token);
            String monument_name = sh2.getString("monument_name",null);
            Log.d("monument_name",monument_name);
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("monument_name",monument_name)
                    .build();
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"/isVerified")
                    .addHeader("Authorization","Bearer "+token)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    InVerificationProcess.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //String isVerify=null;
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        isVerify = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
//                    String finalIsVerify = isVerify;
//                    Log.d("final is verify");
                    InVerificationProcess.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(isVerify.equalsIgnoreCase("true")){
                                startActivity(new Intent(InVerificationProcess.this,AdminBankDetails.class));
                                Log.d("Monument Verified","jhcsjdghv");

                            }else{
                                Toast.makeText(getApplicationContext(),"Monument Verification is in progress....Please wait",Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }
    }
}