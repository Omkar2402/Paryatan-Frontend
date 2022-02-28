package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                                    byte[] bytes = jsonObject.getString("profile_image").getBytes(StandardCharsets.UTF_8);
                                    Log.d("Success:",bytes.toString());
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 400, 400, false));
                                    Log.d("message:",message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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