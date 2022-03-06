package com.example.sihfrontend.user.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sihfrontend.R;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class TicketQR extends AppCompatActivity {

    private ImageView imgQR;
    private TextView tvMonumentName;
    private TextView tvDateOfVisit;
    private RecyclerView recyclerViewQR;
    private TicketQRAdapter ticketQRAdapter;
    private String monumentName;
    private  String date_of_visit;
    private Button downloadDetails;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_qr);
        imgQR = findViewById(R.id.imgQRCode);
        tvMonumentName = findViewById(R.id.tvMonumentNameQR);
        tvDateOfVisit = findViewById(R.id.tvDateofVisit);
        recyclerViewQR = findViewById(R.id.ticketQR_recycler_view);
        downloadDetails = findViewById(R.id.btnDownloadDetails);
        progressBar = findViewById(R.id.ProgressBarQR);
//
//        progressBar.setVisibility(View.VISIBLE);
        //fetchQRImage();

        Intent intent = getIntent();
//        Bundle args = intent.getBundleExtra("bundle");
        ArrayList<ticketInfo>  ticketInfoArrayList = (ArrayList<ticketInfo>) intent.getSerializableExtra("arrayList");
        monumentName = intent.getStringExtra("monumentName");
        date_of_visit = intent.getStringExtra("date_of_visit");

        Toast.makeText(getApplicationContext(),"ArrayList len:"+ticketInfoArrayList.size(),Toast.LENGTH_LONG);

        Log.d("Ticket Info arrayList",""+ticketInfoArrayList.toString());
        ticketQRAdapter = new TicketQRAdapter(TicketQR.this,ticketInfoArrayList){

        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TicketQR.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewQR.setLayoutManager(linearLayoutManager);
        recyclerViewQR.setAdapter(ticketQRAdapter);

        tvMonumentName.setText(monumentName);
        tvDateOfVisit.setText(date_of_visit);

        ticketQRAdapter.notifyDataSetChanged();


        downloadDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TicketQR.this,"Download Details",Toast.LENGTH_LONG).show();
                /*
                Option 1:
                    should download all the details of current activity screen and save in pdf
                Option 2:
                    save QR image from backend server in pdf with little or no text(easy)
                    https://stackoverflow.com/questions/8644459/how-to-convert-byte-array-to-pdf-file-in-android
                */



            }
        });

    }

    private void fetchQRImage() {

        OkHttpClient client = new OkHttpClient();
        //Not calculated bill yet

    }
}