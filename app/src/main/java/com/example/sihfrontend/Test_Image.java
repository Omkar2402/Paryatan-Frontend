package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class Test_Image extends AppCompatActivity {

    ImageView img;
    Button btn_img;
    static String FILEPATH = "";
    static File file = new File(FILEPATH);
    byte[] bytes;
    String imageString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);

        img = findViewById(R.id.img);
        btn_img = findViewById(R.id.btnimg);
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/profile-image")
                            .addHeader("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlQGFiIiwiZXhwIjoxNjQ2MTYwNTQxLCJpYXQiOjE2NDYwNzQxNDF9.yz-qUaxIZxUvHKYbegLKfHZuWJYpoG3qj0wiDAmLOQg")
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
                            if(response.isSuccessful()){
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                    String message = jsonObject.getString("message");
                                    imageString = jsonObject.getString("profile_image");
                                    bytes = Base64.decode(imageString, Base64.DEFAULT);
                                    Log.d("Success:",bytes.toString());
                                    Log.d("message:",message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Test_Image.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("in UI Thread", String.valueOf(bytes.length));
                                        if(imageString!=null){
                                            Log.d("in UI Thread", String.valueOf(imageString.length()));
                                            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                                            Log.d("Compressed Bitmap",compressedBitmap.toString());
                                            img.setImageBitmap(compressedBitmap);
                                            Toast.makeText(getApplicationContext(),"ByteArray created..",Toast.LENGTH_SHORT).show();
//                                            Intent verifyIntent = new Intent(getApplicationContext(), OTPVerification.class);
//                                            verifyIntent.putExtra("email",email.getText().toString());
//                                            verifyIntent.putExtra("otp",otp);
//                                            startActivity(verifyIntent);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}