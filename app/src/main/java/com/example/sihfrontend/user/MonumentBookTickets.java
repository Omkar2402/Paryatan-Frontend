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

import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.monumentAdapter;

import java.util.ArrayList;
import java.util.Date;

public class MonumentBookTickets extends AppCompatActivity implements ticketInterface{
    private ArrayList<ticketInfo> ticketInfoArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TicketAdapter ticket_adapter;
    String monumentName;
    Button dateOfVisit;
    EditText verificationId;

    String gender;
    String age;
    String nationality;

    RadioButton male;
    RadioButton female;
    RadioButton child;
    RadioButton adult;
    RadioButton indian;
    RadioButton foreign;
    EditText visitorName;
    Button addToList;
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
        child = findViewById(R.id.rdoTicketGenderChild);
        adult = findViewById(R.id.rdoTicketGenderAdult);
        indian = findViewById(R.id.rdoTicketIndian);
        foreign = findViewById(R.id.rdoTicketForeign);
        addToList = findViewById(R.id.btnAddTicket);

        recyclerView = findViewById(R.id.ticket_recycler_view);

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
        ticket_adapter = new TicketAdapter(MonumentBookTickets.this, ticketInfoArrayList, this) {

        };

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dateofvisit = new Date(2022, 04, 03);

                ticketInfo ticketInfoobj = new ticketInfo(monumentName, dateofvisit, verificationId.getText().toString(), gender, age, nationality, visitorName.getText().toString());
                ticketInfoArrayList.add(ticketInfoobj);
                ticket_adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(ticket_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MonumentBookTickets.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}