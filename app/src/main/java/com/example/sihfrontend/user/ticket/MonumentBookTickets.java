package com.example.sihfrontend.user.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sihfrontend.R;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonumentBookTickets extends AppCompatActivity implements ticketInterface{
    private ArrayList<ticketInfo> ticketInfoArrayList;
    private RecyclerView recyclerView;
    private Calendar calendar;
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

          //calendar = Calendar.getInstance();
//        int date = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);

        //DatePickerDialog datePickerDialog = new DatePickerDialog(MonumentBookTickets.this);



//        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH,month);
//                calendar.set(Calendar.DAY_OF_MONTH,day);
//                updateLabel();
//            }
//        };





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

        dateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new  DatePickerDialog(MonumentBookTickets.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                try {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(MonumentBookTickets.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, (month+1));
                            calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                            String myFormat = "dd/MM/yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                            dateOfVisit.setText(sdf.format(calendar.getTime()));
                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
                    calendar.add(Calendar.YEAR, 0);
                    dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()+(1000*60*60*24*7));// TODO: used to hide future date,month and year
                    dialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                try{
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateOfVisit.setText(dateFormat.format(calendar.getTime()));
    }


}