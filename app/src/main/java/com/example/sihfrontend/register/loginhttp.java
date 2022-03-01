package com.example.sihfrontend.register;

import android.util.Log;
import android.widget.Toast;

import com.example.sihfrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginhttp {
    public void login(String email,String password,String role,String auth){
        try{
            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("email",email)
                    .add("password",password)
                    .add("role",role)
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/login")
                    .addHeader("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlQGEiLCJleHAiOjE2NDYyMTc4NDEsImlhdCI6MTY0NjEzMTQ0MX0.2r5ccTIZkAqFQsLsGdvPCF_lLmXbTz4AM12FencYeUk")
                    .post(requestBody)
                    .build();

            Log.d("Before Response",requestBody.toString());

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
                            String token = jsonObject.getString("token");
                            Log.d("message:",message);
                            Log.d("token:",token);
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
    public void register(String name, String email, String password, String role, File file){
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name",name)
                    .addFormDataPart("email",email)
                    .addFormDataPart("password",password)
                    .addFormDataPart("role",role)
                    .addFormDataPart("profile-image", "img_name", RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.splash)))
//                                .addFormDataPart("profile-image",email+".jpg",
//                                        RequestBody.create(MediaType.parse("image/*"), file))
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/register")
                    .post(formBody)
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
                            String token = jsonObject.getString("token");
                            Log.d("message:",message);
                            Log.d("token",token);
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
}