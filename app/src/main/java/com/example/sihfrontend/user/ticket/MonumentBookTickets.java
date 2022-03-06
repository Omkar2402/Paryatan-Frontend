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

    private double indian_adult;
    private double indian_child;
    private double foreign_adult;
    private double foreign_child;

    private double fare = 0;

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

        Intent intent = getIntent();
        indian_adult = intent.getDoubleExtra("indian_adult",0.0);
        Log.d("Indian_adult_MonumentDesc",""+indian_adult);
        indian_child = intent.getDoubleExtra("indian_child",0.0);
        foreign_adult = intent.getDoubleExtra("foreign_adult",0.0);
        foreign_child = intent.getDoubleExtra("foreign_child",0.0);


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



        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(visitorName.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please enter your name",Toast.LENGTH_LONG).show();
                    }else if(gender==null || nationality==null || age==null){
                        Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
                    }else if(verificationId.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please enter valid Verification ID",Toast.LENGTH_LONG).show();
                    }else{
                        addToList.setText("Add Ticket");
                        Intent intent1 = getIntent();
                        Log.d("intent",nationality+"_"+age);
                        fare += (intent1.getDoubleExtra(nationality+"_"+age,0.0));
                        proceed.setText("PROCEED("+fare+")");


                        Log.d("fare",""+fare);
                        Date dateofvisit = new Date(2022, 04, 03);

                        ticketInfo ticketInfoobj = new ticketInfo(monumentName, dateofvisit, verificationId.getText().toString(), gender, age, nationality, visitorName.getText().toString());
                        ticketInfoArrayList.add(ticketInfoobj);
                        //ticket_adapter.updateTicketList(ticketInfoArrayList);
                        ticket_adapter.notifyDataSetChanged();
                        visitorName.setText("");
//                        male.setChecked(false);
//                        female.setChecked(false);
//                        indian.setChecked(false);
//                        foreign.setChecked(false);
//                        adult.setChecked(false);
//                        child.setChecked(false);
                        g.clearCheck();
                        n.clearCheck();
                        a.clearCheck();
                        verificationId.setText("");

                    }



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


    @Override
    public void onEditButtonClicked(ticketInfo ticketInfo) {
        Toast.makeText(getApplicationContext(),"Edit Button"+ticketInfo.getVisitorName(),Toast.LENGTH_LONG).show();
        visitorName.setText(ticketInfo.getVisitorName());
        if(ticketInfo.getGender().equalsIgnoreCase("male"))
            g.check(R.id.rdoTicketGenderMale);
        else
            g.check(R.id.rdoTicketGenderFemale);
        if(ticketInfo.getNationality().equalsIgnoreCase("indian"))
            n.check(R.id.rdoTicketIndian);
        else
            n.check(R.id.rdoTicketForeign);

        if(ticketInfo.getAge().equalsIgnoreCase("child"))
            a.check(R.id.rdoTicketChild);
        else
            a.check(R.id.rdoTicketAdult);
        verificationId.setText(ticketInfo.getVerificationId());
        ticketInfoArrayList.remove(ticketInfo);
        Intent intent1 = getIntent();
        Log.d("intent",nationality+"_"+age);
        fare -= (intent1.getDoubleExtra(ticketInfo.getNationality()+"_"+ticketInfo.getAge(),0.0));
        proceed.setText("PROCEED("+fare+")");
        ticket_adapter.notifyDataSetChanged();

        addToList.setText("Save Ticket");
    }

    @Override
    public void onDeleteButtonClick(ticketInfo ticketInfo) {
        Toast.makeText(getApplicationContext(),"Delete Button:"+ticketInfo.getVisitorName(),Toast.LENGTH_LONG).show();
        ticketInfoArrayList.remove(ticketInfo);
        Intent intent1 = getIntent();
        Log.d("intent",nationality+"_"+age);
        fare -= (intent1.getDoubleExtra(ticketInfo.getNationality()+"_"+ticketInfo.getAge(),0.0));
        proceed.setText("PROCEED("+fare+")");
        //ticket_adapter.updateTicketList(ticketInfoArrayList);
        ticket_adapter.notifyDataSetChanged();
    }
}