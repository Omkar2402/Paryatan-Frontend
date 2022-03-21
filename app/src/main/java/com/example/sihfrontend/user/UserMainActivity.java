package com.example.sihfrontend.user;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sihfrontend.MainActivity;
import com.example.sihfrontend.R;
import com.example.sihfrontend.UserProfile;
import com.example.sihfrontend.user.monument.MonumentDescription;
import com.example.sihfrontend.user.monument.MonumentInterface;
import com.example.sihfrontend.user.monument.monumentAdapter;
import com.example.sihfrontend.user.monument.monumentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMainActivity extends AppCompatActivity implements MonumentInterface {


    private RecyclerView recyclerView;
    private ArrayList<monumentInfo> monumentInfoArrayList;
    private ProgressBar progressBar;
    private  monumentAdapter monument_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBarMon);


        monumentInfoArrayList = new ArrayList<>();

        checkredcount();
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
                    .url("http://ec2-18-233-60-31.compute-1.amazonaws.com:8080/monuments")
                    .addHeader("Authorization","Bearer "+token)
                    .get()
                    .build();

            Log.d("Before Response",request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //wait = false;

                    //progressBar.setVisibility(View.GONE);
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
                            Log.d("indian_adult",""+indian_adult);
                            double indian_child = jsonArray.getJSONObject(i).getDouble("indian_child");
                            double foreign_adult = jsonArray.getJSONObject(i).getDouble("foreign_adult");
                            double foreign_child = jsonArray.getJSONObject(i).getDouble("foreign_child");
                            String location = jsonArray.getJSONObject(i).getString("location");
                            String closed_day = jsonArray.getJSONObject(i).getString("closedDay");
                            Log.d("monumnet_name",monument_name);
                            Log.d("monument_img",""+bytes);
                            monumentInfo obj = new monumentInfo(monument_name,bytes,monumentDesc,location,foreign_child,foreign_adult,indian_child,indian_adult,closeTime,monumentLink,startTime,video,closed_day);
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

                        //progressBar.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id==R.id.profile) {
            startActivity(new Intent(UserMainActivity.this, UserProfile.class));
        }
        return true;
    }

    @Override
    public void onCardClicked(monumentInfo mInfo) {
        Toast.makeText(getApplicationContext(),"Monument clicked:"+mInfo.getMonumentName(),Toast.LENGTH_SHORT).show();
        Intent monumentExpand = new Intent(UserMainActivity.this, MonumentDescription.class);
        monumentExpand.putExtra("monumentName",mInfo.getMonumentName());
        monumentExpand.putExtra("location",mInfo.getLocation());
        monumentExpand.putExtra("close_time",mInfo.getCloseTime());
        monumentExpand.putExtra("start_time",mInfo.getSartTime());
        monumentExpand.putExtra("image",mInfo.getMonumentImage());
        monumentExpand.putExtra("desc",mInfo.getMonumentDesc());
        monumentExpand.putExtra("foreign_adult",mInfo.getForeign_adult());
        monumentExpand.putExtra("foreign_child",mInfo.getForeign_child());
        monumentExpand.putExtra("indian_adult",mInfo.getIndian_adult());
        monumentExpand.putExtra("indian_child",mInfo.getIndian_child());
        monumentExpand.putExtra("link",mInfo.getMonumentLink());
        monumentExpand.putExtra("video",mInfo.getMonumentVideo());
        monumentExpand.putExtra("name",mInfo.getMonumentName());
        monumentExpand.putExtra("closed_day",mInfo.getClosedDay());

        startActivity(monumentExpand);

    }

    private void checkredcount() {
        try {
            OkHttpClient client = new OkHttpClient();

            SharedPreferences sharedPreferences = UserMainActivity.this.getSharedPreferences("SIH",Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token",null);
            Log.d("token",token);

            Request request = new Request.Builder()
                    .url("http://ec2-18-233-60-31.compute-1.amazonaws.com:8080/user/check-flag-count")
                    .addHeader("Authorization","Bearer "+token)
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String message = jsonObject.getString("message");
                        Log.d("message",message);
                        if (message.charAt(0)=='F') {
                            if (message.equalsIgnoreCase("Flag count:0")) {
                                Log.d("Clean user","...");
                            }
                            else   {
                                UserMainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UserMainActivity.this,"Please be aware you have got 1 red flag",Toast.LENGTH_LONG).show();
                                        Log.d("One red flag","...");
                                    }
                                });

                            }

                        }
                        else {
                            UserMainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences preferences = UserMainActivity.this.getSharedPreferences("SIH", MODE_PRIVATE);
                                    preferences.edit().remove("role").commit();
                                    preferences.edit().remove("token").commit();
                                    Toast.makeText(getApplicationContext(), "Your account has been blocked due to malpractice", Toast.LENGTH_SHORT).show();
                                    Log.d("Blocked","...");
                                    startActivity(new Intent(UserMainActivity.this, MainActivity.class));

                                }
                            });

                        }
                        Log.d("message",message);
                        //progressBar.setVisibility(View.GONE);
                    }catch (Exception e) {
                        //progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Log.d("catch","...");
                    }

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}