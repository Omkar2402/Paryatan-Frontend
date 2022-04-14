package com.example.sihfrontend.streaming.getStreams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.example.sihfrontend.streaming.ViewStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewAllStreams extends AppCompatActivity implements StreamInterface {

    private RecyclerView recyclerView;
    private ArrayList<StreamDetails> allStreams = new ArrayList<>();
    private ProgressBar progressBar;
    private StreamAdapter streamAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_streams);
        recyclerView = findViewById(R.id.recycler_view_stream);
        progressBar = findViewById(R.id.stream_progress_bar);

        streamAdapter = new StreamAdapter(ViewAllStreams.this,allStreams,this){

        };
        fetchStreams();
        recyclerView.setAdapter(streamAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAllStreams.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void fetchStreams() {
        try {
            SharedPreferences sh = ViewAllStreams.this.getSharedPreferences("SIH",MODE_PRIVATE);
            String token = sh.getString("token",null);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getString(R.string.api)+"/getAllLiveSchedules")
                    .addHeader("Authorization","Bearer "+token)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for(int i=0;i<jsonArray.length();i++){
                            String date_of_live = jsonArray.getJSONObject(i).getString("date_of_live");
                            String time_of_live = jsonArray.getJSONObject(i).getString("time_of_live");
                            String  monument_name = jsonArray.getJSONObject(i).getString("monument_name");
                            StreamDetails streamDetails = new StreamDetails(monument_name,date_of_live,time_of_live);
                            allStreams.add(streamDetails);
                            //streamAdapter.notifyDataSetChanged();
                            ViewAllStreams.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    streamAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCardClicked(StreamDetails streamInfo) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();


        Toast.makeText(getApplicationContext(),streamInfo.getMonument_name(),Toast.LENGTH_LONG).show();
        SharedPreferences sh = ViewAllStreams.this.getSharedPreferences("SIH",MODE_PRIVATE);

        String token = sh.getString("token",null);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getString(R.string.api)+"/getLiveStream/"+streamInfo.getMonument_name())
                .addHeader("Authorization","Bearer "+token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String resourceuri = jsonObject.getString("message");
                    ViewAllStreams.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ViewAllStreams.this, ViewStream.class);
                            intent.putExtra("resourceuri",resourceuri);
                            intent.putExtra("monument name",streamInfo.getMonument_name());
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}