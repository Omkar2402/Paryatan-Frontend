package com.example.sihfrontend.register;

import android.util.Log;

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

    private String token;
    public boolean wait = true;

    public  void upload_image(String token,String path){
        try{
            String auth = "Bearer "+token;
            OkHttpClient client = new OkHttpClient();
//            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("profile-image", "img_name", RequestBody.create(MediaType.parse("image/*"), new File(path)));                 .build();

            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("profile-image", "img_name", RequestBody.create(MediaType.parse("image/*"), new File(path)))
                    .build();

            Request request = new Request.Builder()
                    .url("http://ec2-44-195-177-209.compute-1.amazonaws.com:8080/upload-image")
                    .addHeader("Authorization",auth)
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

    public void login(String email,String password,String role,String token){
        try{
            String auth = "Bearer "+token;
            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("email",email)
                    .add("password",password)
                    .add("role",role)
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-44-195-177-209.compute-1.amazonaws.com:8080/login")
                    .addHeader("Authorization", auth)
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
//        Map<String,String> map = new HashMap<>();
        try{
//            map.put("name",name);
//            map.put("email",email);
//            map.put("password",password);
//            map.put("role",role);
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name",name)
                    .addFormDataPart("email",email)
                    .addFormDataPart("password",password)
                    .addFormDataPart("role",role)
                    .addFormDataPart("profile-image", "img_name", RequestBody.create(MediaType.parse("image/*"), String.valueOf(R.drawable.splash_gif)))
//                                .addFormDataPart("profile-image",email+".jpg",
//                                        RequestBody.create(MediaType.parse("image/*"), file))
                    .build();
            Request request = new Request.Builder()
                    .url("http://ec2-44-195-177-209.compute-1.amazonaws.com:8080/register")
                    .post(formBody)
                    .build();

            Log.d("Before Response",request.toString());

            /*AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            return null;
                        }
                        return response.body().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s != null) {
                    }
                }
            };*/



            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    wait = false;
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String message = jsonObject.getString("message");
                            token = jsonObject.getString("token");
                            Log.d("message:",message);
                            Log.d("token",token);
                            wait = false;
//                            map.put("token",token);

                        } catch (JSONException e) {
//                            map.put("token",null);
                            wait = false;
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){
//            map.put("token",null);
            wait = false;
            e.printStackTrace();
        }

    }


    public  String getToken(){
        return  token;
    }

}
