package com.example.sihfrontend.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.monumentAdapter;
import com.example.sihfrontend.user.monument.monumentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<monumentInfo> monumentInfoArrayList;
    private ProgressBar progressBar;
    boolean wait = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBarMon);

        monumentInfoArrayList = new ArrayList<>();


        fetchData();


        while (wait){
            Log.d("Bad practice of code","...");
        }
        progressBar.setVisibility(View.GONE);

        Log.d("Fetch Data","after" );
        monumentAdapter monument_adapter = new monumentAdapter(UserMainActivity.this,monumentInfoArrayList);
        recyclerView.setAdapter(monument_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserMainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);



    }

    private void fetchData() {

        try{
            OkHttpClient client = new OkHttpClient();

            SharedPreferences sharedPreferences = UserMainActivity.this.getSharedPreferences("SIH",Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token",null);

            Request request = new Request.Builder()
                    .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/monuments")
                    .addHeader("Authorization","Bearer "+token)
                    .get()
                    .build();

            Log.d("Before Response",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    wait = false;
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //JSONObject jsonObject = new JSONObject(response.body().string());
                        //JSONArray jsonArray = jsonObject.getJSONArray();
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Log.d("jsonarray",""+jsonArray);
                        for(int i=0;i<jsonArray.length();i++){

                            String monument_name = jsonArray.getJSONObject(i).getString("monumentName");

                            String monument_img = jsonArray.getJSONObject(i).getString("monumentImg");
                            byte[] bytes = Base64.decode(monument_img,Base64.DEFAULT);
                            Log.d("monumnet_name",monument_name);
                            Log.d("monument_img",""+bytes);
                            monumentInfo obj = new monumentInfo(bytes,monument_name);
                            monumentInfoArrayList.add(obj);
                        }
                        wait = false;


                    } catch (JSONException e) {
                        wait = false;
                        e.printStackTrace();
                    }


                }
            });

        }catch (Exception e){
            wait = false;
            e.printStackTrace();
        }


    }

    //on Pause Method is called when app is closed .Then store


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }
}