package com.example.sihfrontend.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sihfrontend.R;

public class AdminBankDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_bank_details);
    }
}