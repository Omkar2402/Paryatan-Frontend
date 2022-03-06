package com.example.sihfrontend.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sihfrontend.R;
import com.example.sihfrontend.TicketQR;
import com.example.sihfrontend.user.monument.monumentAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MonumentBookTickets extends AppCompatActivity implements ticketInterface{
    private ArrayList<ticketInfo> ticketInfoArrayList;
    private RecyclerView recyclerView;
    private TicketAdapter ticket_adapter;
    String monumentName;
    Button dateOfVisit;
    EditText verificationId;

    String gender;
    String age;
    String nationality;
    RadioGroup g;
    RadioGroup a;
    RadioGroup n;
    RadioButton male;
    RadioButton female;
    RadioButton child;
    RadioButton adult;
    RadioButton indian;
    RadioButton foreign;
    EditText visitorName;
    Button addToList;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument_book_tickets);
        Intent in  = getIntent();
        monumentName = in.getStringExtra("monumentName");
        dateOfVisit = findViewById(R.id.btnTicketDate);
        verificationId = findViewById(R.id.etAadharNumber);
        visitorName = findViewById(R.id.etTicketName);
        male = findViewById(R.id.rdoTicketGenderMale);
        female = findViewById(R.id.rdoTicketGenderFemale);
        child = findViewById(R.id.rdoTicketChild);
        adult = findViewById(R.id.rdoTicketAdult);
        indian = findViewById(R.id.rdoTicketIndian);
        foreign = findViewById(R.id.rdoTicketForeign);
        addToList = findViewById(R.id.btnAddTicket);

        g = findViewById(R.id.rdoTicketGender);
        a = findViewById(R.id.rdoTicketAge);
        n = findViewById(R.id.rdoTicketNationality);
        proceed = findViewById(R.id.btnProceed);
        ticketInfoArrayList = new ArrayList<>();
        ticket_adapter = new TicketAdapter(MonumentBookTickets.this, ticketInfoArrayList, this) {

        };
        recyclerView = findViewById(R.id.ticket_recycler_view);
        recyclerView.setAdapter(ticket_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MonumentBookTickets.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        male.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gender = "male";
            }
        });

        female.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gender="female";
            }
        });

        adult.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                age = "adult";
            }
        });

        child.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                age="child";
            }
        });

        indian.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                nationality = "indian";
            }
        });

        foreign.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                nationality="foreign";
            }
        });


        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Date dateofvisit = new Date(2022, 04, 03);

                    ticketInfo ticketInfoobj = new ticketInfo(monumentName, dateofvisit, verificationId.getText().toString(), gender, age, nationality, visitorName.getText().toString());
                    ticketInfoArrayList.add(ticketInfoobj);
//                    ticket_adapter.updateTicketList(ticketInfoArrayList);
                    ticket_adapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ticketInfoArrayList.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add ticket first",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(MonumentBookTickets.this, TicketQR.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("arrayList",(Serializable) ticketInfoArrayList);
                    intent.putExtra("bundle",bundle);
                    intent.putExtra("monumentName",monumentName);
                    startActivity(intent);
                }
            }
        });

    }

}