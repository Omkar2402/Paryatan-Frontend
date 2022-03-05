package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.sihfrontend.user.MonumentBookTickets;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonumentDescription extends AppCompatActivity {

    private VideoView videoView;
    private TextView name;
    private TextView description;
    private TextView start_time;
    private TextView close_time;
    private TextView closedOn;
    private TextView websiteLink;
    private Button bookTicket;
    private Button monLocation;
    private ProgressBar progressBar;

    private String monument_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_description);
        videoView = findViewById(R.id.videomonumentVideo);
        name = findViewById(R.id.tvMonumentName);
        description = findViewById(R.id.tvDescription);
        start_time = findViewById(R.id.tvStartTime);
        close_time = findViewById(R.id.tvCloseTime);
        closedOn = findViewById(R.id.tvClosedOn);
        websiteLink = findViewById(R.id.tvWebsiteLink);
        bookTicket = findViewById(R.id.btnBookTicket);
        monLocation = findViewById(R.id.btnMonumentLocation);
        progressBar = findViewById(R.id.progressBarVideo);

        try{
            String path = "android.resource://" + getPackageName() + "/" + R.raw.sample_video2;
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {

            Intent intent = getIntent();
            name.setText(intent.getStringExtra("monumentName"));
            description.setText(intent.getStringExtra("desc"));
            websiteLink.setText(intent.getStringExtra("link"));
            start_time.setText(intent.getStringExtra("start_time"));
            close_time.setText(intent.getStringExtra("close_time"));
            closedOn.setText("Closed on:"+intent.getStringExtra("closed_day"));
        }catch (Exception e){
            e.printStackTrace();
        }

        bookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("Book tickets clicked","null");
                    Intent in = new Intent(MonumentDescription.this, MonumentBookTickets.class);
                    in.putExtra("monumentName",name.getText().toString());
                    startActivity(in);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

        progressBar.setVisibility(View.VISIBLE);
//        fetchVideo();

        Intent intent = getIntent();
        monument_Name = intent.getStringExtra("name");
        name.setText(monument_Name);
        description.setText(intent.getStringExtra("desc"));
        websiteLink.setText(intent.getStringExtra("link"));
        start_time.setText(intent.getStringExtra("start_time"));
        close_time.setText(intent.getStringExtra("close_time"));
        closedOn.setText("Closed on:"+intent.getStringExtra("closed_day"));


            }
        });
    }

    private void fetchVideo() {

        SharedPreferences sharedPreferences = MonumentDescription.this.getSharedPreferences("SIH", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token",null);

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("http://ec2-35-169-161-33.compute-1.amazonaws.com:8080/monument/"+monument_Name)
                .addHeader("Authorization","Bearer "+token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String monumentVideo = jsonObject.getString("monumentVideo");
                    byte[] video = Base64.decode(monumentVideo,Base64.DEFAULT);

                    InputStream inputStream = new ByteArrayInputStream(video);
                    OutputStream outputStream = new FileOutputStream(String.valueOf(R.raw.sample_video2));
                    try {

                        outputStream.write(video);
//                        File outputFile = File.createTempFile("file", "mp3", getCacheDir());
//                        outputFile.deleteOnExit();
//                        FileOutputStream fileoutputstream = new FileOutputStream(String.valueOf(R.raw.sample_video2));
//                        fileoutputstream.write(video);
//                        fileoutputstream.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
//                    byte data[] = new byte[4096];
//                    int count;
//                    while ((count = inputStream.read(data)) != -1) {
//                        Log.d("in while loop","while loop");
//                        outputStream.write(data, 0, count);
//                    }

                 }catch (Exception e){
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
                MonumentDescription.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String path = "android.resource://" + getPackageName() + "/" + R.raw.sample_video2;
                            videoView.setVideoURI(Uri.parse(path));
                            videoView.start();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

    }
}