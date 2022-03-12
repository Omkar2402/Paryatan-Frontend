package com.example.sihfrontend.user.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import okhttp3.Request;
import okhttp3.RequestBody;

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
    ArrayList<ticketInfo>  ticketInfoArrayList;
    private  double fare;

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
        ticketInfoArrayList = (ArrayList<ticketInfo>) intent.getSerializableExtra("arrayList");
        monumentName = intent.getStringExtra("monumentName");
        fare = intent.getDoubleExtra("fare",0.0);
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
        int males=0,females =0,indian_adult=0,indian_child=0,foreign_adult=0,foreign_child=0;

        for(int i=0;i<ticketInfoArrayList.size();i++){
            if(ticketInfoArrayList.get(i).getGender().equalsIgnoreCase("male")) males++;
            else females++;
            if(ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("adult") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("indian")) indian_adult++;
            else if(ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("child") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("indian")) indian_child++;
            else if(ticketInfoArrayList.get(i).getAge().equalsIgnoreCase("adult") && ticketInfoArrayList.get(i).getNationality().equalsIgnoreCase("foreign")) foreign_adult++;
            else foreign_child++;
        }


        SharedPreferences sh = TicketQR.this.getSharedPreferences("SIH",MODE_PRIVATE);
        String token = sh.getString("token",null);

        OkHttpClient client = new OkHttpClient();



//        RequestBody requestBody = new Request.Builder()
//                .addHeader("Authorization","Bearer "+token)
//                .post()
//                .build();

        //Not calculated bill yet
        //monument_name,fare,no_of_tickets,indian_adult,foreign_adult,indian_child,foreign_child,males,females,date_of_visit

    }
}