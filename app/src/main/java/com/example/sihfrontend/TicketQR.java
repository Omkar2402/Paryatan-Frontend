package com.example.sihfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sihfrontend.user.ticketInfo;

import java.util.ArrayList;

public class TicketQR extends AppCompatActivity {

    private ImageView imgQR;
    private TextView tvMonumentName;
    private TextView tvDateOfVisit;
    private RecyclerView recyclerViewQR;
    private TicketQRAdapter ticketQRAdapter;
    private String monumentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_qr);
        imgQR = findViewById(R.id.imgQRCode);
        tvMonumentName = findViewById(R.id.tvMonumentNameQR);
        tvDateOfVisit = findViewById(R.id.tvDateofVisit);
        recyclerViewQR = findViewById(R.id.ticketQR_recycler_view);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        ArrayList<ticketInfo>  ticketInfoArrayList = (ArrayList<ticketInfo>) args.getSerializable("arrayList");
        monumentName = intent.getStringExtra("monumentName");

        ticketQRAdapter = new TicketQRAdapter(TicketQR.this,ticketInfoArrayList){

        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TicketQR.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewQR.setLayoutManager(linearLayoutManager);
        recyclerViewQR.setAdapter(ticketQRAdapter);



    }
}