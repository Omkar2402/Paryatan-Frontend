package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

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



        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        description.setText(intent.getStringExtra("desc"));
        websiteLink.setText(intent.getStringExtra("link"));
        start_time.setText(intent.getStringExtra("start_time"));
        close_time.setText(intent.getStringExtra("close_time"));
        closedOn.setText("Closed on:"+intent.getStringExtra("closed_day"));

    }
}