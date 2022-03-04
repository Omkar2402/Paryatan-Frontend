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
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.MonumentInterface;
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

public class UserMainActivity extends AppCompatActivity implements MonumentInterface {


    private RecyclerView recyclerView;
    private ArrayList<monumentInfo> monumentInfoArrayList;
    private ProgressBar progressBar;
    private  monumentAdapter monument_adapter;
    boolean wait = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBarMon);


        monumentInfoArrayList = new ArrayList<>();


        fetchData();


//        while (wait){
//            Log.d("Bad practice of code","...");
//        }

        Log.d("Fetch Data","after" );
         monument_adapter = new monumentAdapter(UserMainActivity.this, monumentInfoArrayList,this) {

        };
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
                    //wait = false;

                    progressBar.setVisibility(View.GONE);
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
                            String monumentDesc = jsonArray.getJSONObject(i).getString("monumentDesc");
                            String monumentLink = jsonArray.getJSONObject(i).getString("monumentLink");
                            String monumentVideo = jsonArray.getJSONObject(i).getString("monumentVideo");
                            byte[] video = Base64.decode(monumentVideo,Base64.DEFAULT);
                            String startTime = jsonArray.getJSONObject(i).getString("startTime");
                            String closeTime = jsonArray.getJSONObject(i).getString("closeTime");
                            double indian_adult = jsonArray.getJSONObject(i).getDouble("indian_adult");
                            double indian_child = jsonArray.getJSONObject(i).getDouble("indian_child");
                            double foreign_adult = jsonArray.getJSONObject(i).getDouble("foreign_adult");
                            double foreign_child = jsonArray.getJSONObject(i).getDouble("foreign_child");
                            String location = jsonArray.getJSONObject(i).getString("location");
                            Log.d("monumnet_name",monument_name);
                            Log.d("monument_img",""+bytes);
                            monumentInfo obj = new monumentInfo(monument_name,bytes,monumentDesc,location,foreign_child,foreign_adult,indian_child,indian_adult,closeTime,monumentLink,startTime,video);
                            monumentInfoArrayList.add(obj);
                        }
                        UserMainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                monument_adapter.notifyDataSetChanged();
                            }
                        });
                        //wait = false;


                    } catch (JSONException e) {

                        progressBar.setVisibility(View.GONE);
                        //wait = false;
                        e.printStackTrace();
                    }


                }
            });

        }catch (Exception e){
            //wait = false;

            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }


    }

    //on Pause Method is called when app is closed .Then store


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public void onCardClicked(monumentInfo mInfo) {
        Toast.makeText(getApplicationContext(),"Monument clicked:"+mInfo.getMonumentName(),Toast.LENGTH_SHORT).show();
    }
}